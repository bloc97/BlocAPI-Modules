/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colour;

import colour.addon.SearchColour;
import addon.Addon;
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
public class Colour extends Module {

    public interface ColourAddon {
        public boolean triggerMessage(MessageReceivedEvent e, TokenAdvancedContainer container);
    }
    
    public Colour(ContainerSettings containerSettings, TokenConverter tokenConverter, BotCommandTrigger commandTrigger) {
        super(containerSettings, tokenConverter, commandTrigger, new SearchColour());
    }
    @Override
    public String getFullName() {
        return "Colour";
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
        return "Colour";
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
                
                ColourAddon ca = (ColourAddon) addon;
                if (ca.triggerMessage(e, container)) {
                    return true;
                }
                container.reset();
            }
        }
        return false;
    }
    
}