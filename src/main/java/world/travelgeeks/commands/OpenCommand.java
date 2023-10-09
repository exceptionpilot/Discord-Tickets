package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.BanManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.TicketWrapper;
import world.travelgeeks.utils.config.Messages;

import java.awt.*;

public class OpenCommand implements ICommand {

    TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();
    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    BanManagement banManagement = TicketBot.getInstance().getBanManagement();


    public EmbedBuilder created(TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("Ticket created in: " + channel.getAsMention()).setColor(Color.decode("#D0F7F4"));
        return builder;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, User user, Messages messages) {

        if (!event.isFromGuild()) {
            event.reply(":x: This command is only proposed for Guilds!").queue();
            return;
        }
        Guild guild = event.getGuild();

        if (event.getOption("member") == null) {
            Member member = event.getMember();
            if (!ticketManagement.hasTicket(guild, member.getIdLong())) {
                event.replyEmbeds(created(ticketWrapper.open(guild, member)).build()).setEphemeral(true).queue();
            } else event.deferReply(true).setContent(":x: You are already in a ticket: " + ticketManagement.getChannel(event.getGuild(), event.getMember().getIdLong()).getAsMention()).queue();
            return;
        }

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply(":x: Missing Permission: " + Permission.ADMINISTRATOR).queue();
            return;
        }
        User targetUser = event.getOption("member").getAsUser();
        if (banManagement.hasBan(event.getGuild(), targetUser)) {
            event.deferReply(true).setContent(":x: The user " + targetUser.getName() + " is ticket banned.").queue();
            return;
        }

        Member member = event.getOption("member").getAsMember();
        if (!ticketManagement.hasTicket(guild, member.getIdLong())) {
            event.replyEmbeds(created(ticketWrapper.open(guild, member)).build()).setEphemeral(true).queue();
        } else event.deferReply(true).setContent(":x: You are already in a ticket: " + ticketManagement.getChannel(event.getGuild(), event.getMember().getIdLong()).getAsMention()).queue();
    }
}
