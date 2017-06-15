/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colour.addon;

import addon.Addon;
import helpers.ColourUtils;
import helpers.ParserUtils;
import helpers.RandomUtils;
import java.util.Map.Entry;
import colour.Colour.ColourAddon;
import container.TokenAdvancedContainer;
import container.detector.TokenDetector;
import container.detector.TokenDetectorContainer;
import container.detector.TokenStringDetector;
import colour.addon.ColourDatabase;
import helpers.NumberUtils;
import modules.help.Help;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import token.Token;

/**
 *
 * @author bowen
 */
public class SearchColour implements ColourAddon {
    
    @Override
    public String getName() {
        return "Colour";
    }

    @Override
    public String getDescription() {
        return "Search for colours or generate random colours";
    }

    @Override
    public String getFullHelp() {
        return "**!colour** <name|#hex> - *Searches for colour*\n" + 
                "**!colour** random <number> - *Generates random colours*";
    }

    @Override
    public String getShortHelp() {
        return "**!colour** <name|#hex> - *Searches for colour*\n" + 
                "**!colour** random <number> - *Generates random colours*";
    }

    @Override
    public int getColour() {
        return ColourUtils.getRandomIntColour();
    }

    @Override
    public short getUid() {
        return 0;
    }

    @Override
    public boolean hasPermissions(IUser user, IChannel channel, IGuild guild) {
        return true;
    }
    
    @Override
    public TokenDetectorContainer getTriggerDetector() {
        return new TokenDetectorContainer(
                new TokenStringDetector("colour")
        );
    }

    @Override
    public boolean triggerMessage(MessageReceivedEvent e, TokenAdvancedContainer container) {
        
        if (container.getAsString().equalsIgnoreCase("colour")) {
            container.next();
            
            if (container.getAsString().equalsIgnoreCase("random")) {
                container.next();
                int number = container.getAsNumber().intValue();

                number = NumberUtils.bound(number, 1, 4);

                if (number == 1) {

                    ColourEntry randomEntry = getRandomColour();

                    EmbedObject eo = getColourEmbed(randomEntry);
                    
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();

                } else {

                    for (int i=0; i<number; i++) {

                        ColourEntry randomEntry = getRandomColour();

                        EmbedObject eo = getColourSmallEmbed(randomEntry);
                        RequestBuffer.request(() -> {
                            return e.getChannel().sendMessage(eo);
                        }).get();
                    }

                }
                return true;
            } else if (!container.getAsString().isEmpty()) {
                int colour = 0;
                String colourHexString = container.getAsString();
                if (colourHexString.startsWith("#")) {
                    colourHexString = colourHexString.substring(1);
                }
                if (ParserUtils.isNumber(colourHexString, 16)) {
                    if (colourHexString.length() == 3) {
                        colourHexString = "" + colourHexString.charAt(0) + '0' + colourHexString.charAt(1) + '0' + colourHexString.charAt(2) + '0';
                    }
                    if (colourHexString.length() != 6) {
                        Help.showHelp(e, this);
                        return false;
                    }
                    try {
                        colour = Integer.parseInt(colourHexString, 16);
                    } catch (NumberFormatException ex) {
                        Help.showHelp(e, this);
                        return false;
                    }
                } else {
                    int index = ColourDatabase.findClosestColourByName(ParserUtils.join(container.getRemainingContent(), ' '));
                    colour = ColourDatabase.intDatabase[index];

                }

                int closestColourIndex = ColourDatabase.findClosestColourByInt(colour);
                ColourEntry entry = getColour(closestColourIndex);

                EmbedObject eo = getColourEmbed(colour, entry);
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
                return true;
            } else {
                Help.showHelp(e, this);
                return true;
            }
        }
        return false;
    }
    
    public static EmbedObject getColourEmbed(int searchedColour, ColourEntry closestEntry) {
        EmbedObject eo = new EmbedObject();
        String hexString = ParserUtils.fillBegin(Integer.toHexString(searchedColour), '0', 6);
        String closestHexString = ParserUtils.fillBegin(Integer.toHexString(closestEntry.getKey()), '0', 6);

        //System.out.println(hexString + " " + closestHexString);
        //EmbedObject.EmbedFieldObject fo = new EmbedObject.EmbedFieldObject("HEX: #" + hexString.toUpperCase(), "RGB: 0 0 0", true);
        //eo.fields = new EmbedObject.EmbedFieldObject[] {fo};
        eo.color = (searchedColour == 0) ? 1 : searchedColour;
        eo.author = new EmbedObject.AuthorObject(closestEntry.getValue(), "http://www.colorhexa.com/" + hexString, null, null);
        eo.description = ((closestEntry.getKey() == searchedColour) ? "" : "*(Closest Colour)*");
        eo.image = new EmbedObject.ImageObject("http://www.colorhexa.com/" + closestHexString.toLowerCase() + ".png", null, 32, 32);
        eo.footer = new EmbedObject.FooterObject("#" + hexString.toUpperCase() + " " + (closestEntry.getKey() == searchedColour ? "" : "(" + closestHexString.toUpperCase() + ")"), null, null);
        return eo;
    }
    public static EmbedObject getColourEmbed(ColourEntry entry) {
        return getColourEmbed(entry.getKey(), entry);
    }
    public static EmbedObject getColourSmallEmbed(ColourEntry entry) {
        EmbedObject eo = new EmbedObject();
        String hexString = ParserUtils.fillBegin(Integer.toHexString(entry.getKey()), '0', 6);
        eo.color = (entry.getKey() == 0) ? 1 : entry.getKey();
        eo.author = new EmbedObject.AuthorObject(entry.getValue(), "http://www.colorhexa.com/" + hexString, null, null);
        eo.footer = new EmbedObject.FooterObject("#" + hexString.toUpperCase(), "http://www.colorhexa.com/" + hexString.toLowerCase() + ".png", null);
        return eo;
    }
    
    public static class ColourEntry implements Entry<Integer, String> {
        private int colour;
        private String name;
        public ColourEntry(int colour, String colourName) {
            this.colour = colour;
            this.name = colourName;
        }
        @Override
        public Integer getKey() {
            return colour;
        }

        @Override
        public String getValue() {
            return name;
        }

        @Override
        public String setValue(String value) {
            String oldName = name;
            if (value != null) {
                name = value;
            } else {
                name = "";
            }
            return oldName;
        }
        
    }
    
    public static ColourEntry getColour(int index) {
        int colour = ColourDatabase.intDatabase[index];
        String name = ColourDatabase.database[index][1];
        
        return new ColourEntry(colour, name);
    }
    public static ColourEntry getRandomColour() {
        int index = RandomUtils.randomArrayIndex(ColourDatabase.database);
        int colour = ColourDatabase.intDatabase[index];
        String name = ColourDatabase.database[index][1];
        
        return new ColourEntry(colour, name);
    }
    
}
