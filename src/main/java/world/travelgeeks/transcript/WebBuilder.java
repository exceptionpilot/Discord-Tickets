package world.travelgeeks.transcript;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.utils.config.Configuration;

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
    private List<TranscriptMessage> transcriptMessages;
    Configuration configuration = TicketBot.getInstance().getConfiguration();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebBuilder(String title, Guild guild, TextChannel channel) {
        logger.info("Building new Transcript for: " + guild.getName());
        this.transcriptMessages = new ArrayList<>();
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
                        message.getEmbeds(),
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
            logger.debug(message.getAuthor() + " : " + message.getMessage());
            for (MessageEmbed embed : message.getEmbeds()) {
                builder.append("<div class=\"embed\">\n" +
                        "            <div class=\"embed-description\">" + embed.getDescription() + "</div>\n" +
                        "        </div>");
            }
        }
        logger.info("Message implementing done.");
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
            logger.info("Directory: " + folder.getPath());
            bufferedWriter = new BufferedWriter(new FileWriter(folder));
            bufferedWriter.write(builder.toString());

            if (configuration.isSFTPActive()) {

                JSch jSch = new JSch();
                Session session = jSch.getSession(configuration.getSftpUser(), configuration.getSftpHost(), (int) configuration.getSftpPort());
                session.setConfig("StrictHostKeyChecking", "no");
                session.setConfig(System.getProperties());
                session.setPassword(configuration.getPassword());
                session.connect();

                logger.debug("Make sure to disable sftp client worker");
                Channel jChannel = session.openChannel("sftp");
                ChannelSftp channelSftp = (ChannelSftp) jChannel;
                channelSftp.connect();

                channelSftp.put(folder.getAbsolutePath(), configuration.getSftpPath());

                channelSftp.disconnect();
                session.disconnect();

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                assert bufferedWriter != null;
                bufferedWriter.close();
                logger.info("Successfully transcript build.");
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}