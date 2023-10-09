package world.travelgeeks.transcript;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class  TranscriptMessage {

    private final String author;
    private final String iconUrl;
    private final String message;
    private final List<MessageEmbed> embed;
    private final boolean isBot;

    public TranscriptMessage(String author, String iconUrl, String message, List<MessageEmbed> embed, boolean isBot) {
        this.author = author;
        this.iconUrl = iconUrl;
        this.message = message;
        this.embed = embed;
        this.isBot = isBot;
    }

    public String getAuthor() {
        return author;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getMessage() {
        return message;
    }

    public List<MessageEmbed> getEmbeds() {
        return embed;
    }

    public boolean isBot() {
        return isBot;
    }
}
