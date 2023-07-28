package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.interfaces.ICommand;

import java.awt.*;

public class HelpCommand implements ICommand {

    @Override
    public void execute(SlashCommandInteractionEvent event, User user) {

        if (!event.isFromGuild()) {
            event.reply(":x: This command is only proposed for Guilds!").queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("**Commands:**\n" +
                "- ticket open|close|add\n" +
                "- setup");

        builder.setColor(Color.decode("#D0F7F4"));
        event.replyEmbeds(builder.build()).setEphemeral(true).queue();
    }
}
