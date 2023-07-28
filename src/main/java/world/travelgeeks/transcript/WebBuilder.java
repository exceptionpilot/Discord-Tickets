package world.travelgeeks.transcript;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebBuilder {

    private final String title;
    private final Guild guild;
    private final TextChannel channel;
    private final List<TranscriptMessage> transcriptMessages = new ArrayList<>();

    public WebBuilder(String title, Guild guild, TextChannel channel) {
        this.transcriptMessages.clear();
        this.title = title;
        this.guild = guild;
        this.channel = channel;
    }

    public WebBuilder addMessage(Message message) {
        this.transcriptMessages.add(
                new TranscriptMessage(
                        message.getAuthor().getName(),
                        message.getAuthor().getAvatarUrl(),
                        message.getContentDisplay(),
                        message.getEmbeds().get(0).getDescription(),
                        message.getAuthor().isBot()));
        return this;
    }

    public void build() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>" + this.title + "'s Transcript</title>\n" +
                "    <link rel=\"stylesheet\" href=\"../../style.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <button id=\"dark-mode-toggle\">DarkMode/WhiteMode</button>\n" +
                "    <div class=\"transcript-container\">");

        Collections.reverse(transcriptMessages);
        for (TranscriptMessage message : this.transcriptMessages) {

            builder.append("<div class=\"message\">")
                    .append("<img src=\"" + message.getIconUrl() + "\" alt=\"Icon\" class=\"profile-picture\">")
                    .append("<div class=\"message-details\">")
                    .append("<div class=\"user\">" + message.getAuthor() + "</div>")
                    .append(message.isBot() ? "<div class=\"bot-badge\">BOT</div>" : " " )
                    .append("</div>")
                    .append("<div class=\"content\">" + message.getMessage() + "</div>\n")
                    .append("</div>");
            if (message.getEmbed() !=null) {
                builder.append("<div class=\"embed\">\n" +
                        "            <div class=\"embed-description\">" + message.getEmbed() + "</div>\n" +
                        "        </div>");
            }

        }
        builder.append("<script>\n" +
                "    const darkModeToggle = document.getElementById('dark-mode-toggle');\n" +
                "    const body = document.body;\n" +
                "\n" +
                "    darkModeToggle.addEventListener('click', () => {\n" +
                "        body.classList.toggle('dark-mode');\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n" +
                "</body>\n" +
                "</html>");

        BufferedWriter bufferedWriter = null;
        try {
            File folder = new File("transcripts/" + this.guild.getIdLong() + "/" + this.channel.getIdLong() + "/index.html");
            if (!folder.exists()) folder.getParentFile().mkdirs();
            bufferedWriter = new BufferedWriter(new FileWriter(folder));
            bufferedWriter.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert bufferedWriter != null;
                bufferedWriter.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}