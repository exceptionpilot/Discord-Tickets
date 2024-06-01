package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.BanManagement;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.config.Messages;

import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class TicketCommand implements ICommand {

    BanManagement banManagement = TicketBot.getInstance().getBanManagement();

    @Override
    public void execute(SlashCommandInteractionEvent event, User user, Messages messages) {

        if (!event.isFromGuild()) {
            event.reply(":x: This command is only proposed for Guilds!").setEphemeral(true).queue();
            return;
        }

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply(":x: Missing Permission: " + Permission.ADMINISTRATOR).setEphemeral(true).queue();
            return;
        }

        // This command is not finished...

        User targetUser = event.getOption("user").getAsUser();
        switch (Objects.requireNonNull(event.getSubcommandName())) {

            case "ban":
                if (!banManagement.hasBan(event.getGuild(), targetUser)) {
                    banManagement.ban(event.getGuild(), targetUser, Timestamp.valueOf(LocalDateTime.now().plus(30, ChronoUnit.DAYS)));
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setDescription("You've banned " + targetUser.getName() + "!");
                    builder.setColor(Color.decode("#D0F7F4"));
                    event.deferReply(true).setEmbeds(builder.build()).queue();
                    break;
                } else event.deferReply(true).setContent(":x: The User " + targetUser.getName() + " already has a ban.").queue();
                break;
            case "unban":
                if (banManagement.hasBan(event.getGuild(), targetUser)) {
                banManagement.unban(event.getGuild(), targetUser);
                EmbedBuilder builder = new EmbedBuilder();
                builder.setDescription("You've unbanned " + targetUser.getName() + "!");
                builder.setColor(Color.decode("#D0F7F4"));
                event.deferReply(true).setEmbeds(builder.build()).queue();
                break;
        } else event.deferReply(true).setContent(":x: The User " + targetUser.getName() + " isn't banned.").queue();
        break;
        }
    }
}
