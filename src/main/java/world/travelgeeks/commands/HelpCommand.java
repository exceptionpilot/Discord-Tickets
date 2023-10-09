package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.config.Messages;

import java.awt.*;

public class HelpCommand implements ICommand {

    @Override
    public void execute(SlashCommandInteractionEvent event, User user, Messages messages) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("This project is [Open-Source](https://github.com/DevChewbacca/Java-Ticket-Bot#commands)!");
        builder.setColor(Color.decode("#D0F7F4"));
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }
}
