package world.travelgeeks.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.TicketWrapper;
import world.travelgeeks.utils.config.Messages;

public class CloseCommand implements ICommand {

    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();

    @Override
    public void execute(SlashCommandInteractionEvent event, User user, Messages messages) {

        if (!event.isFromGuild()) {
            event.reply(":x: This command is only proposed for Guilds!").queue();
            return;
        }

        if (ticketManagement.isTicket(event.getGuild(), event.getChannel().asTextChannel())) {
            event.deferReply().queue();
            ticketWrapper.close(event.getChannel().asTextChannel());
        }else event.deferReply(true).setContent(":x: You are not in a ticket!").queue();
    }
}
