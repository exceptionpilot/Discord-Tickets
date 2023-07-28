package world.travelgeeks.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.interfaces.ICommand;

import java.awt.*;

public class TicketCommand implements ICommand {


    @Override
    public void execute(SlashCommandInteractionEvent event, User user) {

        event.deferReply(true).setContent("Currently in development!").queue();
    }
}
