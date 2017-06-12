/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funfacts;

import addon.Addon;
import container.ContainerSettings;
import container.TokenAdvancedContainer;
import dbot.BotCommandTrigger;
import dbot.Module;
import funfacts.addon.RandomFact;
import modules.Help;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import token.TokenConverter;

/**
 *
 * @author bowen
 */
public class FunFacts extends Module {

    public interface RandomInfoAddon {
        public boolean trigger(MessageReceivedEvent e, TokenAdvancedContainer container);
    }
    
    public FunFacts() {
        super(new RandomFact());
    }
    
    @Override
    public String getFullName() {
        return "Fun Facts";
    }

    @Override
    public String getFullDescription() {
        return "Fun facts for fun people.";
    }

    @Override
    public String getFullInfo() {
        return "Fun facts for fun people.";
    }

    @Override
    public String getShortName() {
        return "Fun Facts";
    }

    @Override
    public String getShortDescription() {
        return "Fun facts, random jokes";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getAuthor() {
        return "Bloc97";
    }

    @Override
    public long getUid() {
        return -13960233l;
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

                RandomInfoAddon ria = (RandomInfoAddon) addon;
                if (ria.trigger(e, container)) {
                    return true;
                }
                container.reset();
            }
        }
        return false;
    }
    
}
