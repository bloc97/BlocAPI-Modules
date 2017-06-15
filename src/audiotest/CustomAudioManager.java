
package audiotest;

import com.sun.jna.ptr.PointerByReference;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.audio.*;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.Lazy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import sx.blah.discord.api.internal.DiscordUtils;
import sx.blah.discord.handle.audio.impl.DefaultProcessor;
import sx.blah.discord.handle.audio.impl.DefaultProvider;

public final class CustomAudioManager implements IAudioManager {

	private final IGuild guild;
	private final IDiscordClient client;
	private final Map<IUser, List<IAudioReceiver>> userReceivers = new ConcurrentHashMap<>();
	private final List<IAudioReceiver> generalReceivers = new CopyOnWriteArrayList<>();
	private volatile IAudioProvider provider = new DefaultProvider();
	private volatile IAudioProvider inProvider = new DefaultProvider();
	private volatile IAudioProcessor processor = new DefaultProcessor();
	private volatile boolean useProcessor = true;

	private final Lazy<PointerByReference> monoEncoder;
	private final Lazy<PointerByReference> stereoEncoder;
	private final Lazy<PointerByReference> stereoDecoder = new Lazy<>(() -> CustomOpusUtil.newDecoder(2));
        
        public CustomAudioManager(IAudioManager manager, int bitRateKbps) {
            this.guild = manager.getGuild();
            this.client = guild.getClient();
            setAudioProcessor(manager.getAudioProcessor());
            setAudioProvider(manager.getAudioProvider());
            monoEncoder = new Lazy<>(() -> CustomOpusUtil.newEncoder(1, bitRateKbps));
            stereoEncoder = new Lazy<>(() -> CustomOpusUtil.newEncoder(2, bitRateKbps));
        }

	@Override
	public void setAudioProvider(IAudioProvider provider) {
		if (provider == null)
			provider = new DefaultProvider();
                this.inProvider = provider;
                this.provider = new IAudioProvider() {
                    @Override
                    public boolean isReady() {
                        return inProvider.isReady();
                    }

                    @Override
                    public byte[] provide() {
                        return sendAudio();
                    }

                    @Override
                    public AudioEncodingType getAudioEncodingType() {
                        return inProvider.getAudioEncodingType().OPUS;
                    }

                    @Override
                    public int getChannels() {
                        return inProvider.getChannels();
                    }
                    
                };
                
		useProcessor = getAudioProcessor().setProvider(this.provider);
	}

	@Override
	public IAudioProvider getAudioProvider() {
		return provider;
	}
        
	public IAudioProvider getAudioInProvider() {
		return inProvider;
	}

	@Override
	public void setAudioProcessor(IAudioProcessor processor) {
		if (processor == null)
			processor = new DefaultProcessor();

		this.processor = processor;
		useProcessor = processor.setProvider(getAudioInProvider());
	}

	@Override
	public IAudioProcessor getAudioProcessor() {
		return processor;
	}

	@Override
	public synchronized void subscribeReceiver(IAudioReceiver receiver) {
		subscribeReceiver(receiver, null);
	}

	@Override
	public synchronized void subscribeReceiver(IAudioReceiver receiver, IUser user) {
		if (user == null) {
			generalReceivers.add(receiver);
		} else {
			userReceivers.computeIfAbsent(user, u -> new CopyOnWriteArrayList<>()).add(receiver);
		}
	}

	@Override
	public synchronized void unsubscribeReceiver(IAudioReceiver receiver) {
		// Check general receivers
		generalReceivers.removeIf(r -> r.equals(receiver));
		// Check user receivers
		userReceivers.values().forEach(list -> list.removeIf(r -> r.equals(receiver)));
	}

	public synchronized byte[] sendAudio() {
		IAudioProcessor processor = getAudioProcessor();
		IAudioProvider provider = useProcessor ? processor : getAudioInProvider();

		return getAudioDataForProvider(provider);
	}

	public synchronized void receiveAudio(byte[] opus, IUser user, char sequence, int timestamp) {
		// Initializing decoder is an expensive op. Don't do it if no one is listening
		if (generalReceivers.size() > 0 || userReceivers.size() > 0) {
			byte[] pcm = CustomOpusUtil.decode(stereoDecoder.get(), opus);
			receiveAudio(opus, pcm, user, sequence, timestamp);
		}
	}

	private void receiveAudio(byte[] opusAudio, byte[] pcmAudio, IUser user, char sequence, int timestamp) {
		generalReceivers.parallelStream().forEach(r -> {
			if (r.getAudioEncodingType() == AudioEncodingType.OPUS) {
				r.receive(opusAudio, user, sequence, timestamp);
			} else {
				r.receive(pcmAudio, user, sequence, timestamp);
			}
		});

		if (userReceivers.containsKey(user)) {
			userReceivers.get(user).parallelStream().forEach(r -> {
				if (r.getAudioEncodingType() == AudioEncodingType.OPUS) {
					r.receive(opusAudio, user, sequence, timestamp);
				} else {
					r.receive(pcmAudio, user, sequence, timestamp);
				}
			});
		}
	}

	@Override
	public IGuild getGuild() {
		return guild;
	}

	private byte[] getAudioDataForProvider(IAudioProvider provider) {
		if (provider.isReady() && !Discord4J.audioDisabled.get()) {
			AudioEncodingType type = provider.getAudioEncodingType();
			int channels = provider.getChannels();
			byte[] data = provider.provide();
			if (data == null)
				data = new byte[0];
			if (type != AudioEncodingType.OPUS) {
				data = CustomOpusUtil.encode(channels == 1 ? monoEncoder.get() : stereoEncoder.get(), data);
			}

			return data;
		}
		return new byte[0];
	}
}