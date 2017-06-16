/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import com.sun.jna.ptr.PointerByReference;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import sx.blah.discord.api.internal.Opus;
import sx.blah.discord.api.internal.OpusUtil;

/**
 *
 * @author bowen
 */


public class OpusEncoder {
    
    private final PointerByReference encoder;
    private boolean isReady;
    
    public OpusEncoder(OpusEncoderProperties.SampleRate hz, OpusEncoderProperties.Channels channels, OpusEncoderProperties.Application application) {
        IntBuffer errorBuffer = IntBuffer.allocate(4);
        encoder = Opus.INSTANCE.opus_encoder_create(hz.getInt32(), channels.getInt32(), application.getInt32(), errorBuffer);
        if (errorBuffer != null && errorBuffer.hasRemaining() && errorBuffer.get() == Opus.OPUS_OK) {
            isReady = true;
        }
    }
    
    public void init(OpusEncoderProperties.SampleRate hz, OpusEncoderProperties.Channels channels, OpusEncoderProperties.Application application) {
        int errorCode = Opus.INSTANCE.opus_encoder_init(encoder, hz.getInt32(), channels.getInt32(), application.getInt32());
        if (errorCode == Opus.OPUS_OK) {
            isReady = true;
        }
    }
    
    public void reset() {
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_RESET_STATE);
    }
    
    public void destroy() {
        Opus.INSTANCE.opus_encoder_destroy(encoder);
        isReady = false;
    }
    
    public boolean isReady() {
        return isReady;
    }
    
    public byte[] encode(short[] pcm16, int frameSize) {
        ShortBuffer nonEncodedBuffer = ShortBuffer.wrap(pcm16);
        ByteBuffer encodedBuffer = ByteBuffer.allocate(4096);

        int result = Opus.INSTANCE.opus_encode(encoder, nonEncodedBuffer, frameSize, encodedBuffer, encodedBuffer.capacity());
        
        if (result < 0) { //Error in encoding!, negative results are error codes
            return new byte[0];
        }
        
        byte[] encoded = new byte[result];
        encodedBuffer.get(encoded);
        return encoded;
    }
    
    public byte[] encodeFloat(float[] pcmf, int frameSize) {
        ByteBuffer encodedBuffer = ByteBuffer.allocate(4096);

        int result = Opus.INSTANCE.opus_encode_float(encoder, pcmf, frameSize, encodedBuffer, encodedBuffer.capacity());
        
        if (result < 0) { //Error in encoding!, negative results are error codes
            return new byte[0];
        }
        
        byte[] encoded = new byte[result];
        encodedBuffer.get(encoded);
        return encoded;
    }
    
    public OpusEncoderProperties.Application getApplication() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_APPLICATION_REQUEST, result);
        return OpusEncoderProperties.getApplication(result.get());
    }
    public OpusEncoderProperties.Bandwidth getBandwidth() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_BANDWIDTH_REQUEST, result);
        return OpusEncoderProperties.getBandwidth(result.get());
    }
    public int getBitrate() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_BITRATE_REQUEST, result);
        return result.get();
    }
    public int getComplexity() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_COMPLEXITY_REQUEST, result);
        return result.get();
    }
    public boolean getDTX() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_DTX_REQUEST, result);
        return result.get() == 1;
    }
    public OpusEncoderProperties.Channels getForceChannels() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_FORCE_CHANNELS_REQUEST, result);
        return OpusEncoderProperties.getChannels(result.get());
    }
    public boolean getInBandFEC() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_INBAND_FEC_REQUEST, result);
        return result.get() == 1;
    }
    public int getLastPacketDuration() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_LAST_PACKET_DURATION_REQUEST, result);
        return result.get();
    }
    public int getLookAhead() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_LOOKAHEAD_REQUEST, result);
        return result.get();
    }
    public int getLSBDepth() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_LSB_DEPTH_REQUEST, result);
        return result.get();
    }
    public OpusEncoderProperties.Bandwidth getMaxBandwidth() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_MAX_BANDWIDTH_REQUEST, result);
        return OpusEncoderProperties.getBandwidth(result.get());
    }
    public int getPacketLossPercentage() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_PACKET_LOSS_PERC_REQUEST, result);
        return result.get();
    }
    public boolean getDisablePrediction() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_PREDICTION_DISABLED_REQUEST, result);
        return result.get() == 1;
    }
    public OpusEncoderProperties.SampleRate getSampleRate() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_SAMPLE_RATE_REQUEST, result);
        return OpusEncoderProperties.getSampleRate(result.get());
    }
    public OpusEncoderProperties.SignalType getSignalType() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_SIGNAL_REQUEST, result);
        return OpusEncoderProperties.getSignalType(result.get());
    }
    public boolean getVBR() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_VBR_REQUEST, result);
        return result.get() == 1;
    }
    public boolean getVBRConstraint() {
        IntBuffer result = IntBuffer.allocate(1);
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_GET_VBR_CONSTRAINT_REQUEST, result);
        return result.get() == 1;
    }
    
    
    public void setApplication(OpusEncoderProperties.Application application) {
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_APPLICATION_REQUEST, application.getInt32());
    }
    public void setBandwidth(OpusEncoderProperties.Bandwidth bandwidth) {
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_BANDWIDTH_REQUEST, bandwidth.getInt32());
    }
    public void setBitrate(int bitRate) {
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_BANDWIDTH_REQUEST, bitRate);
    }
    public void setBitrate(OpusEncoderProperties.SpecialBitrate bitRate) {
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_BANDWIDTH_REQUEST, bitRate.getInt32());
    }
    public void setComplexity(int complexity) {
        complexity = Math.max(0, Math.min(complexity, 10)); //Bound the complexity between 0 and 10
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_COMPLEXITY_REQUEST, complexity);
    }
    public void setDTX(boolean useDTX) {
        int opus_int32 = useDTX ? 1 : 0;
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_COMPLEXITY_REQUEST, opus_int32);
    }
    public void setForceChannels(OpusEncoderProperties.Channels channels) {
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_FORCE_CHANNELS_REQUEST, channels.getInt32());
    }
    public void setInBandFEC(boolean useFEC) {
        int opus_int32 = useFEC ? 1 : 0;
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_INBAND_FEC_REQUEST, opus_int32);
    }
    public void setLSBDepth(int precisionBits) {
        precisionBits = Math.max(8, Math.min(precisionBits, 24)); //Bound the precision between 8 and 24 bits
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_LSB_DEPTH_REQUEST, precisionBits);
    }
    public void setMaxBandwidth(OpusEncoderProperties.Bandwidth bandwidth) {
        if (bandwidth == OpusEncoderProperties.Bandwidth.AUTO) {
            bandwidth = OpusEncoderProperties.Bandwidth.FULLBAND;
        }
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_LSB_DEPTH_REQUEST, bandwidth);
    }
    public void setPacketLossPercentage(int packetLossPercentage) {
        packetLossPercentage = Math.max(0, Math.min(packetLossPercentage, 100)); //Bound the packet loss percentage between 0 and 100 percent
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_PACKET_LOSS_PERC_REQUEST, packetLossPercentage);
    }
    public void setDisablePrediction(boolean disablePrediction) {
        int opus_int32 = disablePrediction ? 1 : 0;
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_PREDICTION_DISABLED_REQUEST, opus_int32);
    }
    public void setSignalType(OpusEncoderProperties.SignalType signalType) {
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_SIGNAL_REQUEST, signalType.getInt32());
    }
    public void setVBR(boolean useVBR) {
        int opus_int32 = useVBR ? 1 : 0;
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_VBR_REQUEST, opus_int32);
    }
    public void setVBRConstraint(boolean isConstrainedVBR) {
        int opus_int32 = isConstrainedVBR ? 1 : 0;
        Opus.INSTANCE.opus_encoder_ctl(encoder, Opus.OPUS_SET_VBR_CONSTRAINT_REQUEST, opus_int32);
    }
    
    
}
