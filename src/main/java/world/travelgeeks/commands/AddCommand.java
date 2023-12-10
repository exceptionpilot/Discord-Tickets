package world.travelgeeks.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.TicketWrapper;
import world.travelgeeks.utils.config.Messages;

import java.awt.*;

public class AddCommand implements ICommand {

    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    GuildManagement guildManagement = TicketBot.getInstance().getGuildManagement();
    TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();

    @Override
    public void execute(SlashCommandInteractionEvent event, User user, Messages messages) {

        if (!event.isFromGuild()) {
            event.reply(messages.getMessage("syntax.guild").getAsString()).queue();
            return;
        }

        Member member = event.getMember();
        if (!member.getRoles().contains(guildManagement.getRole(event.getGuild()))) {
            event.deferReply(true).setContent(messages.getMessage("syntax.missingRole").getAsString()).queue();
            return;
        }

        if (!ticketManagement.isTicket(event.getGuild(), event.getChannel().asTextChannel())) {
            event.reply(messages.getMessage("syntax.noTicket").getAsString()).setEphemeral(true).queue();
            return;
        }

        Member target = event.getOption("member").getAsMember();
        ticketWrapper.add(event.getChannel().asTextChannel(), member);
        ticketWrapper.add(event.getChannel().asTextChannel(), target);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(messages.getMessage("commands.add.embed.description").getAsString()
                .replace("{usernameMention}", target.getAsMention()));
        builder.setColor(Color.decode("#D0F7F4"));
        event.replyEmbeds(builder.build()).queue();

    }
}