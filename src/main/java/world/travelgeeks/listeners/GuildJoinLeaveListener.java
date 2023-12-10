package world.travelgeeks.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.utils.TicketWrapper;
import world.travelgeeks.utils.config.Messages;

public class GuildJoinLeaveListener extends ListenerAdapter {

    Logger logger = LoggerFactory.getLogger(GuildJoinLeaveListener.class);
    TicketWrapper ticketWrapper = TicketBot.getInstance().getTicketWrapper();
    TicketManagement ticketManagement = TicketBot.getInstance().getTicketManagement();
    GuildManagement management = TicketBot.getInstance().getGuildManagement();

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        management.create(event.getGuild());
        logger.info("Joined new Community: " + event.getGuild().getName());
    }

    @Override
    public void onReady(ReadyEvent event) {

        for (Guild guild : event.getJDA().getGuilds()) {
            this.logger.debug("loading probs of " + guild);
            if (!management.exists(guild))
                management.create(guild);
        }

    }


    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        logger.info("incoming delete request " + event.getGuild().getName());
        management.delete(event.getGuild());
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        if (ticketManagement.hasTicket(guild, event.getUser().getIdLong())) {
            ticketWrapper.close(ticketManagement.getChannel(guild, event.getUser().getIdLong()));
        }
    }

}
