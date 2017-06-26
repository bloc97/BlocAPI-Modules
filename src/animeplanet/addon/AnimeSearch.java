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
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author bowen
 */
public class AnimeSearch implements AnimePlanetAddon {

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
    public boolean hasPermissions(MessageReceivedEvent e) {
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
                    e.getChannel().sendMessage("Sorry, could not find the anime \"" + name + "\"").queue();
                    return false;
                }
                
                MessageEmbed eb = getAnimeEmbed(anime);
                e.getChannel().sendMessage(eb);
                
            } catch (Exception ex) {
                e.getChannel().sendMessage("Sorry, could not find the anime \"" + name + "\"").queue();
            }
            return true;
        }
        return false;
    }
    
    public static MessageEmbed getAnimeEmbed(AnimePage anime) {
        
        EmbedBuilder eb = new EmbedBuilder();
        
        eb.setColor(Color.BLACK);
        eb.setAuthor(anime.getTitle(), anime.getUrl(), null);
        eb.setDescription((anime.hasAltTitle() ? "*" + anime.getAltTitle() + "*\n" : "") +
                anime.getType() + " (" + anime.getEpisodes() + " " + (anime.getEpisodes().equalsIgnoreCase("1") ? "ep" : "eps") + ")");
        
        List<String> tags = anime.getTags();
        String tagString = tags.get(0);
        for (int i=1; i<tags.size(); i++) {
            tagString += (i%3 == 0 ? "\n" : ", ")  + tags.get(i);
        }
        
        String description = anime.getDescription();
        if (description.length() > 150) {
            description = description.substring(0, anime.getDescription().indexOf('.', 140)) + "...";
        }
        
        eb.addField("Synopsis", description, true);
        eb.addField("Tags", tagString, true);
        eb.addField("\uFEFF", ":star: " + anime.getRating(), false);
        eb.setThumbnail(anime.getThumbnailUrl());
        eb.setFooter("Rank #" + anime.getRank(), null);
        return eb.build();
    }
}
