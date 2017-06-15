/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotest;

import sx.blah.discord.handle.audio.impl.AudioManager;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;

/**
 *
 * @author bowen
 */
public class CustomAudioPlayer extends AudioPlayer {
    
    public CustomAudioPlayer(IGuild guild, int bitRateKbps) {
        super(new CustomAudioManager(guild.getAudioManager(), bitRateKbps));
    }
    
    
}
