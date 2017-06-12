/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colour;

import colour.addon.SearchColour;
import addon.Addon;
import colour.addon.RandomColour;
import container.ContainerSettings;
import container.TokenAdvancedContainer;
import dbot.BotCommandTrigger;
import dbot.Module;
import sx.blah.discord.api.IDiscordClient;
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
        public boolean triggerMessage(IDiscordClient client, MessageReceivedEvent e, TokenAdvancedContainer container);
    }
    
    public Colour() {
        add(new RandomColour());
        add(new SearchColour());
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
    
    private final static ContainerSettings settings = ContainerSettings.buildSettings("!");
    @Override
    public ContainerSettings getContainerSettings() {
        return settings;
    }

    private final static TokenConverter converter = TokenConverter.getDefault();
    @Override
    public TokenConverter getTokenConverter() {
        return converter;
    }

    private final static BotCommandTrigger trigger = BotCommandTrigger.getDefault(settings);
    @Override
    public BotCommandTrigger getCommandTrigger() {
        return trigger;
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
                if (ca.triggerMessage(getBotClient(), e, container)) {
                    return true;
                }
                container.reset();
            }
        }
        return false;
    }
    
}