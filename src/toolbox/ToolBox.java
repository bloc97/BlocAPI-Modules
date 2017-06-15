/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toolbox;

import addon.Addon;
import container.ContainerSettings;
import container.TokenAdvancedContainer;
import dbot.BotCommandTrigger;
import dbot.ModuleEmptyImpl;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import token.TokenConverter;
import toolbox.ToolBox.ToolBoxAddon;

/**
 *
 * @author bowen
 */
public class ToolBox extends ModuleEmptyImpl<ToolBoxAddon> {

    public static interface ToolBoxAddon extends Addon {
        public boolean triggerMessage(MessageReceivedEvent e, TokenAdvancedContainer container);
    }
    
    public ToolBox(ContainerSettings containerSettings, TokenConverter tokenConverter, BotCommandTrigger commandTrigger) {
        super(containerSettings, tokenConverter, commandTrigger);
    }

    @Override
    public String getFullName() {
        return "Toolbox";
    }

    @Override
    public String getAuthor() {
        return "Bloc97";
    }

    @Override
    public long getUid() {
        return -8631569034l;
    }
    
    @Override
    public boolean onMessageForEachAddon(ToolBoxAddon addon, MessageReceivedEvent e, TokenAdvancedContainer container) {
        return addon.triggerMessage(e, container);
    }
    
}
