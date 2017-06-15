/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animeplanet;

import addon.Addon;
import animeplanet.AnimePlanet.AnimePlanetAddon;
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
public class AnimePlanet extends Module<AnimePlanetAddon> {

    public interface AnimePlanetAddon extends Addon {
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
    public boolean onMessageForEachAddon(AnimePlanetAddon addon, MessageReceivedEvent e, TokenAdvancedContainer container) {
        return addon.triggerMessage(e, container);
    }
    
}
