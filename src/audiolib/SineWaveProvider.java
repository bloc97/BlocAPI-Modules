/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiolib;

import net.dv8tion.jda.core.audio.AudioSendHandler;

/**
 *
 * @author bowen
 */
public class SineWaveProvider implements AudioSendHandler {

    public int ix = 0;
    
    @Override
    public boolean canProvide() {
        return true;
    }

    @Override
    public byte[] provide20MsAudio() {
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
    public boolean isOpus() {
        return false;
    }
    
}
