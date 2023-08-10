package world.travelgeeks.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.BanManagement;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.utils.TicketWrapper;

import java.awt.*;

public class UserContextInteractionListener extends ListenerAdapter {

    TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();
    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    BanManagement banManagement = TicketBot.getInstance().getBanManagement();

    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.deferReply(true).setContent(":x: Missing Permission: " + Permission.ADMINISTRATOR).queue();
            return;
        }

        switch (event.getInteraction().getName()) {
            case "Open Ticket":

                Member member = event.getTargetMember();
                if (banManagement.hasBan(event.getGuild(), event.getTarget())) {
                    event.deferReply(true).setContent(":x: The user " + event.getTarget().getName() + " is ticket banned.").queue();
                    return;
                }

                if (!ticketManagement.hasTicket(event.getGuild(), member.getIdLong())) {
                    event.deferReply(true).queue();
                    TextChannel ticketChannel = ticketWrapper.open(event.getGuild(), member);
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setDescription("Ticket created in: " + ticketChannel.getAsMention()).setColor(Color.decode("#D0F7F4"));
                    event.getHook().editOriginalEmbeds(builder.build()).queue();

                } else event.deferReply(true).setContent(":x: You are already in a ticket: " + ticketManagement.getChannel(event.getGuild(), member.getIdLong()).getAsMention()).queue();
        }
    }
}
