/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.tritonus.share.sampled.AudioSystemShadow;
import sx.blah.discord.api.internal.OpusUtil;

/**
 *
 * @author bowen
 */
public class OpusCompatiblePCMStream extends AudioInputStream {
    
    private OpusCompatiblePCMStream(AudioInputStream stream) {
        super(stream, stream.getFormat(), stream.getFrameLength());
    }
    
    public int getSampleSizeInBytes() {
        return getFormat().getSampleSizeInBits() / 8;
    }
    
    public short[] getFrame(int opusFrameSize) {
        int frameSizeInBytes = opusFrameSize * this.getFormat().getChannels() * getSampleSizeInBytes();
        byte[] b = new byte[frameSizeInBytes];
        
        try {
            int maxLength = read(b);
            return PCMUtils.bigEndianConversion(b, maxLength);
        } catch (IOException ex) {
            return new short[frameSizeInBytes];
        }
    }
    
    public static OpusCompatiblePCMStream createOpusCompatiblePCMStream(AudioInputStream stream) {
        AudioFormat format = stream.getFormat();
        
        if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED && 
            format.isBigEndian() && 
            format.getSampleRate() == getClosestCompatibleSampleRate(format.getSampleRate()) && 
            format.getSampleSizeInBits() == 16) {
            
            return new OpusCompatiblePCMStream(stream);
        }
        
        final float closestSampleRate = getClosestCompatibleSampleRate(format.getSampleRate());
        AudioFormat compatiblePCM = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED, 
            closestSampleRate, 
            16, 
            format.getChannels(), 
            2 * format.getChannels(), //Frame size is 16 bits per channel, so 2 bytes per channel
            closestSampleRate, //Frame rate is exactly the sample rate, as one frame contains one sample
            true
        );
        
        return new OpusCompatiblePCMStream(AudioSystem.getAudioInputStream(compatiblePCM, stream));
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
