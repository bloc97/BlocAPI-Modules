/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotest;

import com.sun.jna.ptr.PointerByReference;
import java.nio.IntBuffer;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.internal.Opus;
import sx.blah.discord.api.internal.OpusUtil;
import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProcessor;
import sx.blah.discord.handle.audio.IAudioProvider;
import sx.blah.discord.handle.audio.impl.DefaultProvider;

/**
 *
 * @author bowen
 */
public final class OpusEncoderProcessor implements IAudioProcessor {
    
    private IAudioProvider provider;
    private boolean isReady;

    public OpusEncoderProcessor() {
        provider = new DefaultProvider();
    }
    
    public OpusEncoderProcessor(IAudioProvider provider) {
        setProvider(provider);
    }
    
    @Override
    public boolean setProvider(IAudioProvider provider) {
        if (!AudioEncodingType.OPUS.equals(provider.getAudioEncodingType())) {
            this.provider = provider;
            this.isReady = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isReady() {
        return provider.isReady() && isReady;
    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return AudioEncodingType.OPUS;
    }

    @Override
    public byte[] provide() {
        if (isReady() && !Discord4J.audioDisabled.get()) {
            int channels = provider.getChannels();
            byte[] data = provider.provide();
            if (data == null)
                data = new byte[0];
            
            return provider.getAudioEncodingType().equals(AudioEncodingType.OPUS) ? 
                data : OpusUtil.encode(channels == 1 ? monoEncoder : stereoEncoder, data);
        }
        return new byte[0];
    }
    
    private final PointerByReference monoEncoder = newEncoder(1);
    private final PointerByReference stereoEncoder = newEncoder(2);
    
    public void setBitRate(int kbps) {
        Opus.INSTANCE.opus_encoder_ctl(monoEncoder, Opus.OPUS_SET_BITRATE_REQUEST, kbps*1000);
        Opus.INSTANCE.opus_encoder_ctl(stereoEncoder, Opus.OPUS_SET_BITRATE_REQUEST, kbps*1000);
    }
    
    public static PointerByReference newEncoder(int channels) {
        return Opus.INSTANCE.opus_encoder_create(OpusUtil.OPUS_SAMPLE_RATE, channels, Opus.OPUS_APPLICATION_AUDIO, IntBuffer.allocate(4));
    }

    public static PointerByReference newDecoder(int channels) {
        return Opus.INSTANCE.opus_decoder_create(OpusUtil.OPUS_SAMPLE_RATE, channels, IntBuffer.allocate(4));
    }   
    
}
