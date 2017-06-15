/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funfacts.addon;

import addon.Addon;
import container.TokenAdvancedContainer;
import container.detector.TokenDetectorContainer;
import container.detector.TokenStringDetector;
import helpers.ParserUtils;
import java.util.Date;
import funfacts.FunFacts.RandomInfoAddon;
import funfacts.HttpHelper;
import modules.help.Help;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.api.internal.json.objects.EmbedObject.EmbedFieldObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.RequestBuffer;
import token.NumberToken;

/**
 *
 * @author bowen
 */
public class RandomFact implements RandomInfoAddon {
    
    @Override
    public String getName() {
        return "Random Facts";
    }
    
    @Override
    public String getDescription() {
        return "Displays fun facts for users.";
    }
    
    @Override
    public String getFullHelp() {
        return "**!funfact** - *Displays a random fun fact*\n" + 
               "**!funfact** <number> - *Displays a fun fact for that number*\n" + 
               "**!funfact** <type> (value) - *Displays a specific type of fun fact*\n\n" + 
               "    Accepted types: \n" +
               "        fact - *Random fact*\n" +
               "        quote - *Random quote*\n" +
               "        insult - *Random insult*\n" +
               "        joke - *Random joke*\n" +
               "        trivia <number> - *Number facts*\n" +
               "        math <number> - *Math facts*\n" +
               "        year <year> - *Year facts*\n" +
               "        date <date> - *Date facts*\n\n" + 
               "*Note: You can replace any value by **random** for a random fun fact.*";
    }
    
    @Override
    public String getShortHelp() {
        return "**!funfact** - *Displays a fun fact*\n" + 
               "**!funfact** <number> - *Displays a fun fact for that number*\n" + 
               "**!funfact** <type> (value) - *Displays a specific type of fun fact, see help page for more info*";
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
                new TokenStringDetector("funfact")
        );
    }
    
    @Override
    public boolean hasPermissions(IUser user, IChannel channel, IGuild guild) {
        return true;
    }
    
    @Override
    public boolean trigger(MessageReceivedEvent e, TokenAdvancedContainer container) {
        if (container.getAsString().equalsIgnoreCase("funfact")) {
            container.next();
            
            if (container.isType(NumberToken.class)) {
                int i = container.getAsNumber().intValue();
                EmbedObject eo = getEmbedObject("Number " + i, getNumberFact(i), "Number Fact");
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
                
            } else if (container.getAsString().isEmpty()) {
                EmbedObject eo = getEmbedObject("Did you know?", getRandomFact(), "Random Fact");
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
                
            } else if (container.getAsString().equalsIgnoreCase("random")) {
                String fact = getRandomNumberFact();
                String i = fact.substring(0, fact.indexOf(" "));
                EmbedObject eo = getEmbedObject("Number " + i, fact, "Number Fact");
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
                
            } else if (container.getAsString().equalsIgnoreCase("trivia")) {
                container.next();
                if (container.isType(NumberToken.class)) {
                    int i = container.getAsNumber().intValue();
                    EmbedObject eo = getEmbedObject("Number " + i, getNumberFact(i), "Number Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                
                } else if (container.getAsString().equalsIgnoreCase("random")) {
                    String fact = getRandomNumberFact();
                    String i = fact.substring(0, fact.indexOf(" "));
                    EmbedObject eo = getEmbedObject("Number " + i, fact, "Number Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                
                } else {
                    Help.showHelp(e, this);
                    
                }
                
            } else if (container.getAsString().equalsIgnoreCase("math")) {
                container.next();
                if (container.isType(NumberToken.class)) {
                    int i = container.getAsNumber().intValue();
                    EmbedObject eo = getEmbedObject("Number " + i, getNumberMathFact(i), "Math Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                    
                } else if (container.getAsString().equalsIgnoreCase("random")) {
                    String fact = getRandomMathFact();
                    String i = fact.substring(0, fact.indexOf(" "));
                    EmbedObject eo = getEmbedObject("Number " + i, fact, "Math Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                    
                } else {
                    Help.showHelp(e, this);
                    
                }
            } else if (container.getAsString().equalsIgnoreCase("year")) {
                container.next();
                if (container.isType(NumberToken.class)) {
                    int i = container.getAsNumber().intValue();
                    EmbedObject eo = getEmbedObject("Year " + i, getYearFact(i), "Year Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                    
                } else if (container.getAsString().equalsIgnoreCase("random")) {
                    String fact = getRandomYearFact();
                    String i = fact.substring(0, fact.indexOf(" "));
                    EmbedObject eo = getEmbedObject("Year " + i, fact, "Year Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                    
                } else {
                    Help.showHelp(e, this);
                    
                }
            } else if (container.getAsString().equalsIgnoreCase("date")) {
                container.next();
                String[] dates = container.getAsString().split("/");
                if (dates.length == 2 && ParserUtils.isNumber(dates[0]) && ParserUtils.isNumber(dates[1])) {
                    String fact = getDateFact(dates[0], dates[1]);
                    String date = fact.substring(0, fact.indexOf(" ", fact.indexOf(" ") + 1));
                    EmbedObject eo = getEmbedObject(date, fact, "Date Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                    
                } else if (container.getAsString().equalsIgnoreCase("random")) {
                    String fact = getRandomDateFact();
                    String date = fact.substring(0, fact.indexOf(" ", fact.indexOf(" ") + 1));
                    EmbedObject eo = getEmbedObject(date, fact, "Date Fact");
                    RequestBuffer.request(() -> {
                        return e.getChannel().sendMessage(eo);
                    }).get();
                    
                } else {
                    Help.showHelp(e, this);
                    
                }
            } else if (container.getAsString().equalsIgnoreCase("fact")) {
                EmbedObject eo = getEmbedObject("Did you know?", getRandomFact(), "Random Fact");
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
                
            } else if (container.getAsString().equalsIgnoreCase("quote")) {
                String[] quote = getRandomQuote();
                EmbedObject eo = getEmbedObject("Quote", quote[0], quote[1]);
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
                
            } else if (container.getAsString().equalsIgnoreCase("insult")) {
                EmbedObject eo = getEmbedObject("Insult", getRandomInsult(), "Random Insult");
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
                
            } else if (container.getAsString().equalsIgnoreCase("joke")) {
                EmbedObject eo = getEmbedObject("Joke", getRandomJoke(), "Random Joke");
                RequestBuffer.request(() -> {
                    return e.getChannel().sendMessage(eo);
                }).get();
            }
            
            return true;
        }
        return false;
    }
    
    private EmbedObject getEmbedObject(String title, String content, String footer) {
        EmbedObject eo = new EmbedObject();
        eo.footer = new EmbedObject.FooterObject(footer, null, null);
        eo.color = getColour();
        EmbedFieldObject fo = new EmbedFieldObject(title, content, false);
        eo.fields = new EmbedFieldObject[] {fo};
        return eo;
    }
    
    private String getRandomFact() {
        String url = "http://www.randomfunfacts.com/";
        String startSearch = "<td bordercolor=\"#FFFFFF\"><font face=\"Verdana\" size=\"4\"><strong><i>";
        String endSearch = "</i></strong></font>";
        return HttpHelper.retrieveFromUrl(url, startSearch, endSearch);
    }
    private String[] getRandomQuote() {
        String url = "http://www.quotability.com/";
        String startSearch = "<td><font face=\"Verdana\" size=\"4\"><strong><i>";
        String endSearch = "</i></strong></font>";
        String quote = HttpHelper.retrieveFromUrl(url, startSearch, endSearch);
        String content = quote.substring(0, quote.lastIndexOf("-"));
        content = content.trim();
        content = content.substring(1, content.length());
        content = content.trim(); //Remove quotation marks
        String attr = quote.substring(quote.lastIndexOf("-"), quote.length());
        return new String[] {content, attr};
    }
    private String getRandomInsult() {
        String url = "http://www.randominsults.net/";
        String startSearch = "<td bordercolor=\"#FFFFFF\"><font face=\"Verdana\" size=\"4\"><strong><i>";
        String endSearch = "</i></strong></font>";
        return HttpHelper.retrieveFromUrl(url, startSearch, endSearch);
    }
    private String getRandomJoke() {
        String url = "http://www.randomfunnyjokes.com/";
        String startSearch = "<td bordercolor=\"#FFFFFF\" height=\"1\"><font face=\"Verdana\" size=\"2\">";
        String endSearch = "</font>";
        return HttpHelper.retrieveFromUrl(url, startSearch, endSearch).replaceAll("<br>", "\n");
    }
    
    
    private String getNumberFact(int i) {
        String url = "http://numbersapi.com/" + i + "/trivia";
        String string = HttpHelper.getFromUrl(url);
        while (string.contains("is a number for which we're missing a fact")) {
            string = HttpHelper.getFromUrl(url);
        }
        return string;
    }
    private String getRandomNumberFact() {
        String url = "http://numbersapi.com/random/trivia";
        return HttpHelper.getFromUrl(url);
    }
    
    private String getNumberMathFact(int i) {
        String url = "http://numbersapi.com/" + i + "/math";
        String string = HttpHelper.getFromUrl(url);
        while (string.contains("is a number for which we're missing a fact")) {
            string = HttpHelper.getFromUrl(url);
        }
        return string;
    }
    private String getRandomMathFact() {
        String url = "http://numbersapi.com/random/math";
        return HttpHelper.getFromUrl(url);
    }
    
    private String getYearFact(int i) {
        if (i == 0) {
            
            return (Math.random() > 0.5) ? "Did you know? Some people believe that year 0 exists." : 
                    "Jesus was not born in the year 0.";
        }
        String url = "http://numbersapi.com/" + i + "/year";
        String string = HttpHelper.getFromUrl(url);
        
        return string;
    }
    private String getRandomYearFact() {
        String url = "http://numbersapi.com/random/year";
        return HttpHelper.getFromUrl(url);
    }
    
    private String getDateFact(String month, String date) {
        String url = "http://numbersapi.com/" + month + "/" + date + "/date";
        String string = HttpHelper.getFromUrl(url);
        return string;
    }
    private String getDateFact(Date date) {
        String url = "http://numbersapi.com/" + (date.getMonth() + 1) + "/" + date.getDate() + "/math";
        String string = HttpHelper.getFromUrl(url);
        return string;
    }
    private String getRandomDateFact() {
        String url = "http://numbersapi.com/random/date";
        return HttpHelper.getFromUrl(url);
    }
    
}
