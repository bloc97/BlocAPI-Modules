/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import com.sun.jna.ptr.PointerByReference;
import com.sun.media.sound.AudioSynthesizer;
import com.sun.media.sound.SoftSynthesizer;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import net.dv8tion.jda.core.audio.AudioSendHandler;

/**
 *
 * @author bowen
 */
public class AudioSynthStreamProviderFactory {
    
    private final static Map<String, Object> info = new ConcurrentHashMap();
    private final static float SAMPLERATE = 48000;
    private final static int CHANNELS = 2;
    private final static int BITRATE = 16;
    private final static int FRAMESIZE = (BITRATE/8) * CHANNELS;
    private final static AudioFormat format = new AudioFormat(48000, 16, 2, true, true);
    
    
    
    public static AudioSendHandler getProvider(AudioSynthesizer synth) {
        
        
        try {
            return new AudioSendHandler() {
                
                private final AudioInputStream stream = synth.openStream(format, info);
                
                private boolean isClosed = false;
                
                @Override
                public boolean canProvide() {
                    try {
                        return stream.available() >= 0 && !isClosed;
                    } catch (IOException ex) {
                        return false;
                    }
                }

                @Override
                public byte[] provide20MsAudio() {
                    int totalSize = (int)(SAMPLERATE * 20d/1000d * FRAMESIZE);
                    if (totalSize % 4 != 0) totalSize = totalSize - (totalSize%4);
                    System.out.println(totalSize);
                    
                    byte[] b = new byte[totalSize];
                    try {
                        int maxLength = stream.read(b);
                        if (maxLength < 0) {
                            stream.close();
                            isClosed = true;
                        }
                    } catch (IOException ex) {
                        return b;
                    }
                System.out.println(b[0] << 8 | b[1]);
                    return b;
                }

                @Override
                public boolean isOpus() {
                    return false;
                }

            };
            
        } catch (MidiUnavailableException ex) {
            return new AudioSendHandler() {
                @Override
                public boolean canProvide() {
                    return false;
                }

                @Override
                public byte[] provide20MsAudio() {
                    return new byte[0];
                }
            };
        }
        
        
    }
    
}
