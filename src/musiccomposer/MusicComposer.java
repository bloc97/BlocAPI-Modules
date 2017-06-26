/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musiccomposer;

import addon.AddonEmptyImpl;
import audiolib.AudioSynthStreamProviderFactory;
import music.Music.AudioAddon;
import com.sun.media.sound.AudioSynthesizer;
import com.sun.media.sound.SoftSynthesizer;
import container.TokenAdvancedContainer;
import container.detector.TokenDetectorContainer;
import container.detector.TokenStringDetector;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author bowen
 */
public class MusicComposer extends AddonEmptyImpl implements AudioAddon {
    
    private final AudioSynthesizer synth;
    
    public MusicComposer() {
        AudioSynthesizer tempSynth;
        try {
            tempSynth = (AudioSynthesizer) MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException | ClassCastException ex) {
            tempSynth = new SoftSynthesizer();
        }
        synth = tempSynth;
    }
    
    @Override
    public String getName() {
        return "Composer";
    }

    @Override
    public short getUid() {
        return 1;
    }

    @Override
    public TokenDetectorContainer getTriggerDetector() {
        return new TokenDetectorContainer(new TokenStringDetector("compose"));
    }

    @Override
    public boolean hasPermissions(MessageReceivedEvent e) {
        return true;
    }

    @Override
    public boolean onMessage(MessageReceivedEvent e, TokenAdvancedContainer container) {
        if (container.getAsString().equalsIgnoreCase("comp")) {
            /*
            //e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
            VoiceChannel voiceChannel = e.getGuild().getAudioManager().getConnectedChannel();
            //IVoiceChannel voiceChannel = e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild()).getChannel();
            if (voiceChannel == null) {
                return false;
            }
            
            AudioPlayer player = AudioPlayer.getAudioPlayerForGuild(e.getGuild());

            synth.getChannels()[0].programChange(20);

            synth.getChannels()[0].noteOn(60, 100);


            try { 
                Thread.sleep(100); // wait time in milliseconds to control duration
            } catch( InterruptedException ex ) { 

            }
            player.queue(AudioSynthStreamProviderFactory.getProvider(synth));
            */
            return true;
        }
        return false;
    }
    
}
