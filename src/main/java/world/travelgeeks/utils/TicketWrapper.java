package world.travelgeeks.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.transcript.WebBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TicketWrapper {

    String apiUrl = "https://api.travelgeeks.world/ticket/"; // own api coming soon...
    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    GuildManagement guildManagement = TicketBot.getInstance().getGuildManagement();
    EnumSet<Permission> permissions = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY);



    public TicketWrapper() {
        this.apiUrl = TicketBot.getInstance().getConfiguration().getApiLink();
    }

    public TextChannel open(Guild guild, Member member) {

        ChannelAction<TextChannel> channelChannelAction = guild.createTextChannel("ticket-" + guildManagement.addTicketCount(guild));
        channelChannelAction.addRolePermissionOverride(guild.getPublicRole().getIdLong(),null , permissions);
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
                .queue();

        return channel;
    }

    public TicketWrapper close(TextChannel channel) {
        Member member = ticketManagement.getMember(channel.getGuild(), channel);
        System.out.println(member);

        String link = transcript(channel);
        System.out.println("Link: " + link);
        ticketManagement.delete(channel.getGuild(), (Member) member);
        channel.delete().queue();
        sendPrivateMessage(member.getUser(), "Transcript: " + link);
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
        channel.getManager().putMemberPermissionOverride(member.getIdLong(), permissions, null).queue();
        channel.getManager().putRolePermissionOverride(guildManagement.getRole(channel.getGuild()).getIdLong(), null, permissions).queue();
        return this;
    }

    public TextChannelManager setTopic(TextChannel channel, String topic) {
        // TODO: Make async handel
        return channel.getManager().setTopic(topic);
    }
    public String transcript(TextChannel channel) {
        WebBuilder webBuilder = new WebBuilder(channel.getGuild().getName(), channel.getGuild(), channel);
        MessageHistory history = MessageHistory.getHistoryFromBeginning(channel).complete();
        List<Message> messages = history.getRetrievedHistory();
        for (Message message : messages) {
            webBuilder.addMessage(message);
        }
        webBuilder.build();
        return apiUrl + channel.getGuild().getIdLong() + "/" + channel.getIdLong();
    }

    public void sendPrivateMessage(User user, String content) {
        try {

        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(content))
                .queue();

        }catch (Exception exception) {
            // TODO: Catch action
        }
    }

    public void sendWelcome(TextChannel ticketChannel, Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Support will be with you shortly. Click the button underneath this message to close the ticket.").setColor(Color.decode("#D0F7F4"));
        embedBuilder.setFooter(ticketChannel.getGuild().getName(), ticketChannel.getGuild().getIconUrl());
        ticketChannel.sendMessage("G'day " + member.getAsMention() + "!").addEmbeds(embedBuilder.build()).addActionRow(Button.danger("close_ticket", "Close")).queue();
    }
}
