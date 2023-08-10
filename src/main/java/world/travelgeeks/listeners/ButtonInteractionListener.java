package world.travelgeeks.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.BanManagement;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.utils.TicketWrapper;

import java.awt.*;

public class ButtonInteractionListener extends ListenerAdapter {

    TicketManagement management = TicketBot.getInstance().getTicketManagement();
    GuildManagement guildManagement = TicketBot.getInstance().getGuildManagement();
    BanManagement banManagement = TicketBot.getInstance().getBanManagement();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();
        switch (event.getButton().getId()) {
            case "open_ticket":

                if (banManagement.hasBan(event.getGuild(), event.getUser())) {
                    event.deferReply(true).setContent(":x: Action canceled. You are ticket banned.").queue();
                    return;
                }

                if (!management.hasTicket(event.getGuild(), event.getMember().getIdLong())) {
                    event.deferReply(true).queue();
                    TextChannel ticketChannel = ticketWrapper.open(event.getGuild(), event.getMember());
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setDescription("Ticket created in: " + ticketChannel.getAsMention()).setColor(Color.decode("#D0F7F4"));
                    event.getHook().editOriginalEmbeds(builder.build()).queue();

                } else event.deferReply(true).setContent(":x: You are already in a ticket: " + management.getChannel(event.getGuild(), event.getMember().getIdLong()).getAsMention()).queue();

                break;

            case "close_ticket":
                if (management.isTicket(event.getGuild(), event.getChannel().asTextChannel())) {
                    event.deferReply().queue();
                    ticketWrapper.close(event.getChannel().asTextChannel());
                } else event.deferReply(true).setContent(":x: You are not in a ticket!").queue();
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
                event.editButton(Button.secondary("claim_ticket", "Claim").asDisabled()).queue();

                break;
            case "transcript_ticket":
                // TODO: Impl Method
        }
    }
}
