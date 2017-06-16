/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import javax.sound.sampled.AudioInputStream;
import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProvider;

/**
 *
 * @author bowen
 */
public class OpusEncoderProvider implements IAudioProvider {
    
    private final OpusEncoder encoder;
    private final AudioInputStream stream;
    
    public OpusEncoderProvider(OpusEncoder encoder, AudioInputStream stream) {
        this.encoder = encoder;
        this.stream = stream;
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public byte[] provide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getChannels() {
        return IAudioProvider.super.getChannels(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return IAudioProvider.super.getAudioEncodingType(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
