/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioProvider;

/**
 *
 * @author bowen
 */
public class SineWaveProvider implements IAudioProvider {

    public int ix = 0;
    
    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public byte[] provide() {
        byte[] sine = new byte[(int)(48000*0.02*2*2)];
        for (int i=0; i<sine.length; i++) {
            if (ix % 400 < 200) {
                sine[i] = -64;
            } else {
                sine[i] = 64;
            }
            ix++;
        }
        return sine;
    }

    @Override
    public int getChannels() {
        return 2;
    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return AudioEncodingType.PCM;
    }
    
}
