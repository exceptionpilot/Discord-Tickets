package world.travelgeeks.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.utils.TicketWrapper;

public class GuildJoinLeaveListener extends ListenerAdapter {

    TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();
    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    GuildManagement management = TicketBot.getInstance().getGuildManagement();

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        management.create(event.getGuild());
    }

    @Override
    public void onReady(ReadyEvent event) {

        for (Guild guild : event.getJDA().getGuilds()) {
            if (!management.exists(guild))
                management.create(guild);

        }
    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        management.delete(event.getGuild());
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        if (ticketManagement.hasTicket(event.getGuild(), event.getMember())) {
            ticketWrapper.close(ticketManagement.getChannel(event.getGuild(), event.getMember()));
        }
    }
}
