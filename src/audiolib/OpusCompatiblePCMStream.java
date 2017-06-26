/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 *
 * @author bowen
 */
public class OpusCompatiblePCMStream {
    
    private final AudioInputStream stream;
    private boolean isClosed = false;
    
    private OpusCompatiblePCMStream(AudioInputStream stream) {
        this.stream = stream;
    }
    
    public int getSampleSizeInBytes() {
        return stream.getFormat().getSampleSizeInBits() / 8;
    }
    
    public AudioInputStream getStream() {
        return stream;
    }
    public AudioFormat getFormat() {
        return stream.getFormat();
    }
    
    public boolean isClosed() {
        return isClosed;
    }
    
    public short[] getFrame(int opusFrameSize) {
        int frameSizeInBytes = opusFrameSize * stream.getFormat().getFrameSize();
        
        if (isClosed) return new short[frameSizeInBytes / 2];
        
        byte[] b = new byte[frameSizeInBytes];
        
        try {
            int maxLength = stream.read(b);
            
            if (maxLength < 0) {
                isClosed = true;
                stream.close();
                return new short[frameSizeInBytes / 2];
            }
            
            return PCMUtils.bigEndianConversion(b, maxLength);
        } catch (IOException ex) {
            return new short[frameSizeInBytes / 2];
        }
    }
    
    public static OpusCompatiblePCMStream createOpusCompatiblePCMStream(AudioInputStream stream) {
        AudioFormat format = stream.getFormat();
        
        if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED && 
            format.isBigEndian() && 
            format.getSampleSizeInBits() == 16 &&
            format.getSampleRate() == getClosestCompatibleSampleRate(format.getSampleRate())) {
            return new OpusCompatiblePCMStream(stream);
        }
        
        AudioFormat PCM = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 
            format.getSampleRate(), 
            16, 
            format.getChannels(), 
            2 * format.getChannels(), //Frame size is 16 bits per channel, so 2 bytes per channel
            format.getSampleRate(), //Frame rate is exactly the sample rate, as one frame contains one sample
            true
        );
        AudioInputStream newPCMStream = AudioSystem.getAudioInputStream(PCM, stream); //Convert whatever format to PCM
        
        final float closestSampleRate = getClosestCompatibleSampleRate(format.getSampleRate());
        AudioFormat compatiblePCM = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 
            closestSampleRate, 
            16, 
            format.getChannels(), 
            2 * format.getChannels(),
            closestSampleRate,
            true
        );
        AudioInputStream compatibleNewPCMStream = AudioSystem.getAudioInputStream(compatiblePCM, newPCMStream); //Convert to closest sample rate
        
        return new OpusCompatiblePCMStream(compatibleNewPCMStream);
    }
    
    public static float getClosestCompatibleSampleRate(float sampleRate) {
        if (sampleRate > 48000) {
            return 48000;
        } else if (sampleRate > 24000) {
            return 48000;
        } else if (sampleRate > 16000) {
            return 24000;
        } else if (sampleRate > 12000) {
            return 16000;
        } else if (sampleRate > 8000) {
            return 12000;
        } else {
            return 8000;
        }
    }
}
