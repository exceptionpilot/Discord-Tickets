package world.travelgeeks.interfaces;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ICommand {
    void execute(SlashCommandInteractionEvent event, User user);
}
