/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import net.dv8tion.jda.core.audio.AudioSendHandler;

/**
 *
 * @author bowen
 */
public abstract class OpusEncoderProviderFactory {
    
    public static AudioSendHandler getProvider(OpusEncoder encoder, File file) throws IOException, UnsupportedAudioFileException {
        return getProvider(encoder, AudioSystem.getAudioInputStream(file));
    }
    public static AudioSendHandler getProvider(OpusEncoder encoder, URL url) throws IOException, UnsupportedAudioFileException {
        return getProvider(encoder, AudioSystem.getAudioInputStream(url));
    }
    public static AudioSendHandler getProvider(OpusEncoder encoder, InputStream stream) throws IOException, UnsupportedAudioFileException {
        return getProvider(encoder, AudioSystem.getAudioInputStream(stream));
    }
    
    public static AudioSendHandler getProvider(OpusEncoder encoder, AudioInputStream stream) {
        return getProvider(encoder, OpusCompatiblePCMStream.createOpusCompatiblePCMStream(stream));
    }
    
    public static AudioSendHandler getProvider(OpusEncoder encoder, OpusCompatiblePCMStream opusCompatiblePCMStream) {
        
        
        PointerByReference encoderPointer = encoder.getEncoder(opusCompatiblePCMStream.getFormat().getSampleRate(), opusCompatiblePCMStream.getFormat().getChannels());
        
        return new AudioSendHandler() {
            @Override
            public boolean canProvide() {
                try {
                    return opusCompatiblePCMStream.getStream().available() >= 0 && !opusCompatiblePCMStream.isClosed();
                } catch (IOException ex) {
                    return false;
                }
            }
            
            @Override
            public byte[] provide20MsAudio() {
                int opusFrameSize = (int) (opusCompatiblePCMStream.getFormat().getSampleRate() * (20d / 1000d));
                byte[] data = OpusEncoder.encode(encoderPointer, opusCompatiblePCMStream.getFrame(opusFrameSize), opusFrameSize);
                return data;
            }

            @Override
            public boolean isOpus() {
                return true;
            }
            
        };
        
    }
    
    
}
