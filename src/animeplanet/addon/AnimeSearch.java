/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animeplanet.addon;

import addon.Addon;
import animeplanet.AnimePlanet.AnimePlanetAddon;
import animeplanetapi.AnimePage;
import animeplanetapi.Searchers;
import container.TokenAdvancedContainer;
import container.detector.TokenDetectorContainer;
import container.detector.TokenStringDetector;
import helpers.ParserUtils;
import java.util.LinkedList;
import java.util.List;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.api.internal.json.objects.EmbedObject.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;

/**
 *
 * @author bowen
 */
public class AnimeSearch implements Addon, AnimePlanetAddon {

    @Override
    public String getName() {
        return "Anime Search";
    }

    @Override
    public String getDescription() {
        return "Displays anime info for users.";
    }

    @Override
    public String getFullHelp() {
        return "**!anime** <name> - *Displays the anime's basic info*";
    }

    @Override
    public String getShortHelp() {
        return "**!anime** <name> - *Displays the anime's basic info*";
    }

    @Override
    public int getColour() {
        return 5563639;
    }

    @Override
    public short getUid() {
        return 0;
    }

    @Override
    public TokenDetectorContainer getTriggerDetector() {
        return new TokenDetectorContainer(
                new TokenStringDetector("anime")
        );
    }

    @Override
    public boolean hasPermissions(IUser user, IChannel channel, IGuild guild) {
        return true;
    }

    @Override
    public boolean triggerMessage(MessageReceivedEvent e, TokenAdvancedContainer container) {
        if (container.getAsString().equalsIgnoreCase("anime")) {
            container.next();
            String name = container.getRemainingContentAsString();
            try {

                AnimePage anime = Searchers.getAnimeByName(name);
                
                if (anime.getId() == -1) {
                    RequestBuffer.request(() -> {
                        return e.getMessage().reply("Sorry, could not find the anime \"" + name + "\"");
                    }).get();
                    return false;
                }
                
                EmbedObject eo = getAnimeEmbed(anime);
                
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
            } catch (Exception ex) {
                RequestBuffer.request(() -> {
                    return e.getMessage().reply("Sorry, could not find the anime \"" + name + "\"");
                }).get();
            }
            return true;
        }
        return false;
    }
    
    public static EmbedObject getAnimeEmbed(AnimePage anime) {
        EmbedObject eo = new EmbedObject();
        
        eo.color = 0;
        eo.author = new AuthorObject(anime.getTitle(), anime.getUrl(),null, null);
        eo.description = (anime.hasAltTitle() ? "*" + anime.getAltTitle() + "*\n" : "") +
                anime.getType() + " (" + anime.getEpisodes() + " " + (anime.getEpisodes().equalsIgnoreCase("1") ? "ep" : "eps") + ")";
        
        List<EmbedFieldObject> efo = new LinkedList();
        List<String> tags = anime.getTags();
        String tagString = tags.get(0);
        for (int i=1; i<tags.size(); i++) {
            tagString += (i%3 == 0 ? "\n" : ", ")  + tags.get(i);
        }
        
        String description = anime.getDescription();
        if (description.length() > 150) {
            description = description.substring(0, anime.getDescription().indexOf('.', 140)) + "...";
        }
        
        efo.add(new EmbedFieldObject("Synopsis", description, true));
        efo.add(new EmbedFieldObject("Tags", tagString, true));
        efo.add(new EmbedFieldObject("\uFEFF", ":star: " + anime.getRating(), false));
        eo.thumbnail = new ThumbnailObject(anime.getThumbnailUrl(), null, 180, 180);
        eo.fields = efo.toArray(new EmbedFieldObject[0]);
        eo.footer = new FooterObject("Rank #" + anime.getRank(), null, null);
        return eo;
    }
}
