package audiotest;

import com.sun.jna.ptr.PointerByReference;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import sx.blah.discord.api.internal.Opus;

public class CustomOpusUtil {

    public static final int OPUS_SAMPLE_RATE = 48000;
    public static final int OPUS_FRAME_SIZE = 960;
    public static final int OPUS_FRAME_TIME = 20;

    public static PointerByReference newEncoder(int channels, int bitRateKbps) {
        PointerByReference encoder = Opus.INSTANCE.opus_encoder_create(OPUS_SAMPLE_RATE, channels, Opus.OPUS_APPLICATION_AUDIO, IntBuffer.allocate(4));
        //Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_SIGNAL_REQUEST, Opus.OPUS_SIGNAL_MUSIC);
        //Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_PACKET_LOSS_PERC_REQUEST, 0);
        //Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_PREDICTION_DISABLED_REQUEST, 1);
        //Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_EXPERT_FRAME_DURATION_REQUEST, Opus.OPUS_FRAMESIZE_20_MS);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_BITRATE_REQUEST, 64*1000);
        return encoder;
    }

    public static PointerByReference newDecoder(int channels) {
            return Opus.INSTANCE.opus_decoder_create(OPUS_SAMPLE_RATE, channels, IntBuffer.allocate(4));
    }
    
    public static byte[] encode(PointerByReference encoder, byte[] pcm) {
        ShortBuffer nonEncodedBuffer = ShortBuffer.allocate(pcm.length / 2);
        ByteBuffer encodedBuffer = ByteBuffer.allocate(4096);
        for (int i = 0; i < pcm.length; i += 2) {
                int firstByte = (0x000000FF & pcm[i]);
                int secondByte = (0x000000FF & pcm[i + 1]);

                short combined = (short) ((firstByte << 8) | secondByte);
                nonEncodedBuffer.put(combined);
        }
        nonEncodedBuffer.flip();

        int result = Opus.INSTANCE.opus_encode(encoder, nonEncodedBuffer, OPUS_FRAME_SIZE, encodedBuffer, encodedBuffer.capacity());

        byte[] encoded = new byte[result];
        encodedBuffer.get(encoded);
        return encoded;
    }
    
    public static byte[] decode(PointerByReference decoder, byte[] opus) {
        ShortBuffer decodedBuffer = ShortBuffer.allocate(4096);

        int result = Opus.INSTANCE.opus_decode(decoder, opus, opus.length, decodedBuffer, OPUS_FRAME_SIZE, 0);

        short[] shortAudio = new short[result * 2];
        decodedBuffer.get(shortAudio);

        ByteBuffer byteBuffer = ByteBuffer.allocate(shortAudio.length * 2);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        byteBuffer.asShortBuffer().put(shortAudio);
        return byteBuffer.array();
    }
}