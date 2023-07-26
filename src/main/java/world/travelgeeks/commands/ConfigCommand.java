package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.config.Configuration;

import java.awt.*;

public class ConfigCommand extends ListenerAdapter implements ICommand {

    Configuration configuration = TicketBot.getInstance().getConfiguration();
    GuildManagement management = TicketBot.getInstance().getGuildManagement();

    public EmbedBuilder welcome (Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#D0F7F4"));
        builder.setDescription("Click the button underneath this message.");
        builder.setFooter(guild.getName(), guild.getIconUrl());
        return  builder;
    }

    public EmbedBuilder success (Guild guild) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("Your settings have been adjusted.");
        builder.setColor(Color.decode("#D0F7F4"));
        builder.setFooter(guild.getName(), guild.getIconUrl());
        return  builder;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, User user) {

        if (!event.isFromGuild()) {
            event.reply(":x: This command is only proposed for Guilds!").queue();
            return;
        }

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply(":x: Missing Permission: " + Permission.ADMINISTRATOR).queue();
            return;
        }

        if (event.getOption("category").getAsChannel().asCategory() ==null) {
            event.reply(":x: Your arguments must provide a category!").queue();
            return;
        }

        Role role = event.getOption("role").getAsRole();
        Category category = event.getOption("category").getAsChannel().asCategory();
        management.setCategory(event.getGuild(), category);
        management.setRole(event.getGuild(), role);

        event.replyEmbeds(success(event.getGuild()).build()).setEphemeral(true).queue(ctx -> {
            ctx.sendMessageEmbeds(welcome(ctx.getInteraction().getGuild()).build()).addActionRow(Button.success("open_ticket", "Open")).queue();
        });
    }
}