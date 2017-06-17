/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotest;

import addon.Addon;
import audiolib.OpusEncoder;
import audiolib.OpusEncoderProperties;
import audiolib.OpusEncoderProviderFactory;
import container.ContainerSettings;
import container.TokenAdvancedContainer;
import dbot.BotCommandTrigger;
import dbot.ModuleEmptyImpl;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.audio.AudioPlayer;
import token.TokenConverter;

/**
 *
 * @author bowen
 */
public class AudioTest extends ModuleEmptyImpl {
    
    private final OpusEncoder encoder = new OpusEncoder(OpusEncoderProperties.Application.AUDIO);
    
    public AudioTest(ContainerSettings containerSettings, TokenConverter tokenConverter, BotCommandTrigger commandTrigger) {
        super(containerSettings, tokenConverter, commandTrigger);
    }

    @Override
    public String getFullName() {
        return "Audio Test";
    }

    @Override
    public String getAuthor() {
        return "Bloc97";
    }

    @Override
    public long getUid() {
        return -419738612l;
    }
    
    @Override
    public boolean onMessage(MessageReceivedEvent e, TokenAdvancedContainer container) {
        
        if (container.getAsString().equalsIgnoreCase("play")) {
            
            int kbps = 64;
            int index = 1;
            
            container.next();
            if (container.getAsNumber() > 0 && container.getAsNumber() < 7) {
                index = container.getAsNumber().intValue() - 1;
            }
            IVoiceChannel voiceChannel = e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild()).getChannel();
            
            if (voiceChannel == null) {
                IVoiceChannel userVoiceChannel = e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel();
                if (userVoiceChannel == null) {
                    return false;
                }
                RequestBuffer.request(() -> {
                    userVoiceChannel.join();
                }).get();
            }
            
            AudioPlayer player = AudioPlayer.getAudioPlayerForGuild(e.getGuild());
            
            //OpusEncoderProcessor coe = new OpusEncoderProcessor();
            
            //player.addProcessor(coe);
            //coe.setBitRate(kbps);
            
            File[] musicFiles = new File("music").listFiles();
            
            player.clear();
            
            try {
                player.queue(OpusEncoderProviderFactory.getProvider(encoder, musicFiles[index]));
            } catch (IOException | UnsupportedAudioFileException ex) {
                System.out.println(ex);
            }
            return true;
            
        } else if (container.getAsString().equalsIgnoreCase("stop")) {
            IVoiceChannel voiceChannel = e.getClient().getOurUser().getVoiceStateForGuild(e.getGuild()).getChannel();
            if (voiceChannel == null) {
                IVoiceChannel userVoiceChannel = e.getAuthor().getVoiceStateForGuild(e.getGuild()).getChannel();
                if (userVoiceChannel == null) {
                    return false;
                }
                userVoiceChannel.leave();
                return true;
            }
            voiceChannel.leave();
            return true;
        }
        return false;
        
    }

    @Override
    public boolean onMessageForEachAddon(Addon addon, MessageReceivedEvent e, TokenAdvancedContainer container) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
