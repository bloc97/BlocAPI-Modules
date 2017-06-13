/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animeplanet;

import addon.Addon;
import animeplanet.addon.AnimeSearch;
import container.ContainerSettings;
import container.TokenAdvancedContainer;
import dbot.BotCommandTrigger;
import dbot.Module;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import token.TokenConverter;

/**
 *
 * @author bowen
 */
public class AnimePlanet extends Module {

    public interface AnimePlanetAddon {
        public boolean triggerMessage(MessageReceivedEvent e, TokenAdvancedContainer container);
    }
    
    public AnimePlanet(ContainerSettings containerSettings, TokenConverter tokenConverter, BotCommandTrigger commandTrigger) {
        super(containerSettings, tokenConverter, commandTrigger, new AnimeSearch());
    }
    @Override
    public String getFullName() {
        return "Anime-Planet";
    }

    @Override
    public String getFullDescription() {
        return "";
    }

    @Override
    public String getFullInfo() {
        return "";
    }

    @Override
    public String getShortName() {
        return "Anime";
    }

    @Override
    public String getShortDescription() {
        return "";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getAuthor() {
        return "Bloc97";
    }

    @Override
    public long getUid() {
        return -9123564l;
    }

    @Override
    public boolean onOtherEvent(Event e) {
        return false;
    }

    @Override
    public boolean onReady(ReadyEvent e) {
        return false;
    }

    @Override
    public boolean onMessage(MessageReceivedEvent e, TokenAdvancedContainer container) {
        
        for (Addon addon : getAddons()) {
            if (addon.hasPermissions(e.getAuthor(), e.getChannel(), e.getGuild())) {
                
                AnimePlanetAddon ca = (AnimePlanetAddon) addon;
                if (ca.triggerMessage(e, container)) {
                    return true;
                }
                container.reset();
            }
        }
        return false;
    }
    
}
