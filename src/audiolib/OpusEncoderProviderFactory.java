/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import sx.blah.discord.api.internal.OpusUtil;
import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProvider;

/**
 *
 * @author bowen
 */
public abstract class OpusEncoderProviderFactory {
    
    
    public IAudioProvider getProvider(OpusEncoder encoder, AudioInputStream stream) {
        
        return new IAudioProvider() {
            @Override
            public boolean isReady() {
                try {
                    return stream.available() > 0;
                } catch (IOException ex) {
                    return false;
                }
            }

            @Override
            public byte[] provide() {
                int opusFrameSize = (int) (stream.getFormat().getSampleRate() * 0.02);
                return encoder.encode(OpusCompatiblePCMStream.createOpusCompatiblePCMStream(stream).getFrame(opusFrameSize), opusFrameSize);
            }

            @Override
            public AudioEncodingType getAudioEncodingType() {
                return AudioEncodingType.OPUS;
            }
            
        };
        
        
        
    }
    
    
}
