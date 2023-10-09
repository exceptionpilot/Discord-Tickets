package world.travelgeeks.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.LoggingManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.transcript.WebBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TicketWrapper {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    String apiUrl = "https://api.travelgeeks.world/ticket/"; // own api coming soon...
    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    GuildManagement guildManagement = TicketBot.getInstance().getGuildManagement();
    LoggingManagement loggingManagement = TicketBot.getInstance().getLoggingManagement();
    private EnumSet<Permission> permissions = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY);


    public TicketWrapper() {
        this.apiUrl = TicketBot.getInstance().getConfiguration().getWebpageUrl();
    }

    public TextChannel open(Guild guild, Member member) {
        ChannelAction<TextChannel> channelChannelAction = guild.createTextChannel("ticket-" + guildManagement.addTicketCount(guild));
        channelChannelAction.addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, permissions);
        channelChannelAction.addMemberPermissionOverride(member.getIdLong(), permissions, null);
        channelChannelAction.addRolePermissionOverride(guildManagement.getRole(guild).getIdLong(), permissions, null);
        channelChannelAction.setParent(guildManagement.getCategory(guild));
        channelChannelAction.setTopic(member.getEffectiveName());
        TextChannel channel = channelChannelAction.complete();
        ticketManagement.create(guild, member, channel);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Support will be with you shortly. Click the button underneath this message to close the ticket.");
        embedBuilder.setColor(Color.decode("#D0F7F4"));
        channel.sendMessage("G'day " + member.getAsMention() + "!")
                .addEmbeds(embedBuilder.build())
                .addActionRow(
                        Button.danger("close_ticket", "Close"),
                        Button.secondary("claim_ticket", "Claim")
                )
                .queue(ctx -> {
                    Role role = guildManagement.getRole(guild);
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setDescription("Member " + member.getEffectiveName() + " created a new ticket: " + channel.getAsMention());
                    builder.setColor(Color.decode("#D0F7F4"));
                    guildManagement.getLogChannel(guild).sendMessage(role.getAsMention()).addEmbeds(builder.build()).queue(
                            (message) -> loggingManagement.create(guild, member, message));
                });

        logger.info("Guild-Action @" + member.getEffectiveName() + " -> Ticket#open [" + channel.getGuild().getIdLong() + "]");
        return channel;
    }

    public TicketWrapper close(TextChannel channel) {
        User user = channel.getJDA().retrieveUserById(ticketManagement.getMemberById(channel.getGuild(), channel)).complete();
        String url = this.transcript(channel);

        Message message = loggingManagement.getMessage(channel.getGuild(), user.getIdLong());
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("This ticket has already been **closed**.");
        builder.addField("User:", user.getEffectiveName(), true);
        builder.addField("Ticket:", channel.getName(), true);
        builder.setColor(Color.decode("#D0F7F4"));
        message.editMessageEmbeds(builder.build()).setActionRow(Button.link(url, "Transcript")).queue();

        this.sendPrivateMessage(user, "Your ticket has been closed.", url);

        ticketManagement.delete(channel.getGuild(), user.getIdLong());
        loggingManagement.delete(channel.getGuild(), user.getIdLong());
        channel.delete().queue();
        logger.info("Guild-Action @" + user.getEffectiveName() + " -> Ticket#close [" + channel.getGuild().getIdLong() + "]");
        return this;
    }

    public TicketWrapper add(TextChannel channel, Member member) {
        channel.getManager().putMemberPermissionOverride(member.getIdLong(), permissions, null).queue();
        return this;
    }

    public TicketWrapper remove(TextChannel channel, Member member) {
        channel.getManager().putMemberPermissionOverride(member.getIdLong(), null, permissions).queue();
        return this;
    }

    public TicketWrapper claim(TextChannel channel, Member member) {
        TextChannelManager textChannelManager = channel.getManager();
        textChannelManager.putRolePermissionOverride(guildManagement.getRole(channel.getGuild()).getIdLong(), null, permissions);
        textChannelManager.putMemberPermissionOverride(member.getIdLong(), permissions, null);
        textChannelManager.queue();
        Message message = loggingManagement.getMessage(channel.getGuild(), ticketManagement.getMemberById(channel.getGuild(), channel));
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("This ticket is already in progress.");
        builder.addField("Claim:", member.getEffectiveName(), true);
        builder.setColor(Color.decode("#D0F7F4"));
        message.editMessageEmbeds(builder.build()).queue();
        logger.info("Guild-Action @" + member.getEffectiveName() + " -> Ticket#claim [" + channel.getGuild().getIdLong() + "]");
        return this;
    }

    public TextChannelManager setTopic(TextChannel channel, String topic) {
        // TODO: Make async handel
        return channel.getManager().setTopic(topic);
    }

    public EnumSet<Permission> getPermissions() {
        return this.permissions;
    }

    public String transcript(TextChannel channel) {
        WebBuilder webBuilder = new WebBuilder(channel.getGuild().getName(), channel.getGuild(), channel);
        MessageHistory history = MessageHistory.getHistoryFromBeginning(channel).complete();
        List<Message> messages = history.getRetrievedHistory();
        for (Message message : messages) {
            webBuilder.addMessage(message);
        }
        webBuilder.build();
        String url = apiUrl + channel.getGuild().getIdLong() + "/" + channel.getIdLong();

        logger.info("Guild-Action " + url + " -> Ticket#transcript [" + channel.getGuild().getIdLong() + "]");
        return apiUrl + channel.getGuild().getIdLong() + "/" + channel.getIdLong();
    }

    public TicketWrapper sendPrivateMessage(User user, String content, String url) {
        user.openPrivateChannel().flatMap(
                channel -> channel.sendMessage(content).addActionRow(Button.link(url, "Transcript")))
                .queue(null, new ErrorHandler()
                        .handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                (exception) -> logger.info("Private-Action @" + user.getName() + " -> Ticket#sendPrivateMessage [Cannot send private message]")));
        return this;
    }
}
