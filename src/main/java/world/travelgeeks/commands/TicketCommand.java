package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.TicketWrapper;

import java.awt.*;

public class TicketCommand implements ICommand {


    @Override
    public void execute(SlashCommandInteractionEvent event, User user) {

        TicketWrapper ticketBuilder = new TicketWrapper();
        TextChannel ticketChannel = ticketBuilder.open(event.getGuild(), event.getMember());

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("Ticket created in: " + ticketChannel.getAsMention()).setColor(Color.decode("#D0F7F4"));
        builder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription("Support will be with you shortly. Click the button underneath this message to close the ticket.").setColor(Color.decode("#D0F7F4"));
        embedBuilder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
        ticketChannel.sendMessage("G'day " + user.getAsMention() + "!")
                .addEmbeds(embedBuilder.build())
                .addActionRow(Button.danger("close_ticket", "Close"))
                .queue();
    }
}
