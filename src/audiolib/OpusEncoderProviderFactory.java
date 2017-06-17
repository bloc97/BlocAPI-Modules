/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import sx.blah.discord.api.internal.OpusUtil;
import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProvider;

/**
 *
 * @author bowen
 */
public abstract class OpusEncoderProviderFactory {
    
    public static IAudioProvider getProvider(OpusEncoder encoder, File file) throws IOException, UnsupportedAudioFileException {
        return getProvider(encoder, AudioSystem.getAudioInputStream(file));
    }
    public static IAudioProvider getProvider(OpusEncoder encoder, URL url) throws IOException, UnsupportedAudioFileException {
        return getProvider(encoder, AudioSystem.getAudioInputStream(url));
    }
    public static IAudioProvider getProvider(OpusEncoder encoder, InputStream stream) throws IOException, UnsupportedAudioFileException {
        return getProvider(encoder, AudioSystem.getAudioInputStream(stream));
    }
    
    public static IAudioProvider getProvider(OpusEncoder encoder, AudioInputStream stream) {
        return getProvider(encoder, OpusCompatiblePCMStream.createOpusCompatiblePCMStream(stream));
    }
    
    public static IAudioProvider getProvider(OpusEncoder encoder, OpusCompatiblePCMStream opusCompatiblePCMStream) {
        
        return new IAudioProvider() {
            @Override
            public boolean isReady() {
                try {
                    return opusCompatiblePCMStream.getStream().available() >= 0 && !opusCompatiblePCMStream.isClosed();
                } catch (IOException ex) {
                    return false;
                }
            }
            
            @Override
            public byte[] provide() {
                int opusFrameSize = (int) (opusCompatiblePCMStream.getFormat().getSampleRate() * (OpusUtil.OPUS_FRAME_TIME / 1000d));
                byte[] data = encoder.encode(opusCompatiblePCMStream.getFrame(opusFrameSize), opusFrameSize, opusCompatiblePCMStream.getFormat().getSampleRate(), opusCompatiblePCMStream.getFormat().getChannels());
                return data;
            }

            @Override
            public AudioEncodingType getAudioEncodingType() {
                return AudioEncodingType.OPUS;
            }
            
        };
        
    }
    
    
}
