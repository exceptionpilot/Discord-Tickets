package world.travelgeeks.transcript;

public class TranscriptMessage {

    private final String author;
    private final String iconUrl;
    private final String message;
    private final String embed;
    private final boolean isBot;

    public TranscriptMessage(String author, String iconUrl, String message, String embed, boolean isBot) {
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

    public String getEmbed() {
        return embed;
    }

    public boolean isBot() {
        return isBot;
    }
}
