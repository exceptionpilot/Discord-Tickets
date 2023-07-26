package world.travelgeeks.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.utils.TicketWrapper;

import java.awt.*;

public class ButtonInteractionListener extends ListenerAdapter {

    TicketManagement management = TicketBot.getInstance().getTicketManagement();
    GuildManagement guildManagement = TicketBot.getInstance().getGuildManagement();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        TicketWrapper ticketWrapper = new TicketWrapper();
        switch (event.getButton().getId()) {
            case "open_ticket":

                if (management.hasTicket(event.getGuild(), event.getMember())) {

                    TextChannel ticketChannel = ticketWrapper.open(event.getGuild(), event.getMember());
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setDescription("Ticket created in: " + ticketChannel.getAsMention()).setColor(Color.decode("#D0F7F4"));
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();

                } else event.deferReply(true).setContent(":x: You are already in a ticket: " + management.getChannel(event.getGuild(), event.getMember()).getAsMention()).queue();

                break;

            case "close_ticket":
                if (management.isTicket(event.getGuild(), event.getChannel().asTextChannel())) {
                    ticketWrapper.close(event.getChannel().asTextChannel());
                } else {
                    event.replyEmbeds(new EmbedBuilder().setDescription("You are not in a Ticket!").build()).queue();
                }
                break;
            case "claim_ticket":

                Member member = event.getMember();
                if (!member.getRoles().contains(guildManagement.getRole(event.getGuild()))) {
                    event.deferReply(true).setContent(":x: Missing Role: MISSING_ROLE").queue();
                    return;
                }

                ticketWrapper.claim(event.getChannel().asTextChannel(), event.getMember());
                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription("Your ticket will be progressed by " + event.getMember().getAsMention());
                builder.setColor(Color.decode("#D0F7F4"));
                event.replyEmbeds(builder.build()).queue();

                break;
            case "transcript_ticket":
                // TODO: Impl Method
        }
    }
}