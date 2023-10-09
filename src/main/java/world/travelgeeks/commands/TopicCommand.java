package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.config.Messages;

import java.awt.*;

public class TopicCommand implements ICommand {

    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();

    @Override
    public void execute(SlashCommandInteractionEvent event, User user, Messages messages) {
        if (!event.isFromGuild()) {
            event.reply(messages.getMessage("syntax.guild").getAsString()).setEphemeral(true).queue();
            return;
        }

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply(":x: Missing Permission: " + Permission.ADMINISTRATOR).setEphemeral(true).queue();
            return;
        }

        if (!ticketManagement.isTicket(event.getGuild(), event.getChannel().asTextChannel())) {
            event.reply(":x: You must be in a ticket to execute this command.").setEphemeral(true).queue();
            return;
        }

        String topic = event.getOption("topic").getAsString();
        event.getChannel().asTextChannel().getManager().setTopic(topic).submit()
                .thenComposeAsync((ctx) ->
                        event.replyEmbeds(new EmbedBuilder().setDescription("Topic has been changed to: " + topic).setColor(Color.decode("#D0F7F4")).build()
                ).submit());
    }
}
