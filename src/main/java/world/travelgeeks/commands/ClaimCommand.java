package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.TicketWrapper;
import world.travelgeeks.utils.config.Messages;

import java.awt.*;

public class ClaimCommand implements ICommand {

    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    GuildManagement guildManagement = TicketBot.getInstance().getGuildManagement();
    TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();

    @Override
    public void execute(SlashCommandInteractionEvent event, User user, Messages messages) {


        if (!event.isFromGuild()) {
            event.reply(":x: This command is only proposed for Guilds!").queue();
            return;
        }

        Member member = event.getMember();
        if (!member.getRoles().contains(guildManagement.getRole(event.getGuild()))) {
            event.deferReply(true).setContent(":x: Missing Role: MISSING_ROLE").queue();
            return;
        }

        if (!ticketManagement.isTicket(event.getGuild(), event.getChannel().asTextChannel())) {
            event.reply(":x: You must be in a ticket to execute this command.").setEphemeral(true).queue();
            return;
        }

        ticketWrapper.claim(event.getChannel().asTextChannel(), event.getMember());
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("Your ticket will be progressed by " + event.getMember().getAsMention());
        builder.setColor(Color.decode("#D0F7F4"));
        event.replyEmbeds(builder.build()).queue();

    }
}