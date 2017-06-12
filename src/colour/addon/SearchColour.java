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
import modules.colour.ColourDatabase;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import token.Token;

/**
 *
 * @author bowen
 */
public class SearchColour implements Addon, ColourAddon {
    
    private int searchColour = ColourUtils.getIntFromRGB(1f, 1f, 1f);
    
    @Override
    public String getName() {
        return "Random Colour";
    }

    @Override
    public String getDescription() {
        return "Generates random colours.";
    }

    @Override
    public String getFullHelp() {
        return "**!colour** random - *Generates random colours*";
    }

    @Override
    public String getShortHelp() {
        return "**!colour** random - *Generates random colours*";
    }

    @Override
    public int getColour() {
        return searchColour;
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
                new TokenStringDetector("colour"),
                new TokenDetector() {
                    @Override
                    public boolean check(Token token) {
                        String colourToken = token.getString();
                        if (colourToken.startsWith("#") && (colourToken.length() == 4 || colourToken.length() == 7)) {
                            return true;
                        } else if (colourToken.equalsIgnoreCase("random")) {
                            return false;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean triggerMessage(IDiscordClient client, MessageReceivedEvent e, TokenAdvancedContainer container) {
        
        if (container.getAsString().equalsIgnoreCase("colour") && !container.getNextAsString().equalsIgnoreCase("random")) {
            
            container.next();
            
            int colour = 0;
            String colourHexString = container.getAsString();
            if (colourHexString.startsWith("#")) {
                colourHexString = colourHexString.substring(1);
                if (colourHexString.length() == 3) {
                    colourHexString = "" + colourHexString.charAt(0) + '0' + colourHexString.charAt(1) + '0' + colourHexString.charAt(2) + '0';
                }
                if (colourHexString.length() != 6) {
                    return false;
                }
                try {
                    colour = Integer.parseInt(colourHexString, 16);
                } catch (NumberFormatException ex) {
                    return false;
                }
            } else {
                int index = ColourDatabase.findClosestColourByName(colourHexString);
                colour = ColourDatabase.intDatabase[index];
                
            }
            
            int closestColourIndex = ColourDatabase.findClosestColourByInt(colour);
            
            int closestColour = ColourDatabase.intDatabase[closestColourIndex];
            String closestColourName = ColourDatabase.database[closestColourIndex][1];
            
            
            EmbedObject eo = new EmbedObject();
            String hexString = ParserUtils.fillBegin(Integer.toHexString(colour), '0', 6);
            String closestHexString = ParserUtils.fillBegin(Integer.toHexString(closestColour), '0', 6);
            
            //System.out.println(hexString + " " + closestHexString);
            //EmbedObject.EmbedFieldObject fo = new EmbedObject.EmbedFieldObject("HEX: #" + hexString.toUpperCase(), "RGB: 0 0 0", true);
            //eo.fields = new EmbedObject.EmbedFieldObject[] {fo};
            eo.footer = new EmbedObject.FooterObject("#" + hexString.toUpperCase(), null, null);
            eo.color = (colour == 0) ? 1 : colour;
            eo.image = new EmbedObject.ImageObject("http://www.colorhexa.com/" + closestHexString + ".png", null, 32, 32);
            eo.author = new EmbedObject.AuthorObject(closestColourName, "http://www.colorhexa.com/" + hexString, null, null);
            eo.description = ((closestColour == colour) ? "" : "*(Closest Colour)*");
            e.getChannel().sendMessage(eo);
            return true;
        }
        return false;
    }
    
    public static Entry<Integer, String> getRandomColour() {
        int index = RandomUtils.randomArrayIndex(ColourDatabase.database);
        int colour = ColourDatabase.intDatabase[index];
        String name = ColourDatabase.database[index][1];
            
        final int finalColour = colour;
        final String finalName = name;
        return new Entry<Integer, String>() {
            @Override
            public Integer getKey() {
                return finalColour;
            }

            @Override
            public String getValue() {
                return finalName;
            }

            @Override
            public String setValue(String value) {
                return finalName;
            }
        };
    }
    
}
