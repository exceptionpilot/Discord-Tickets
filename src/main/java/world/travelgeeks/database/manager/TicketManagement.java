package world.travelgeeks.database.manager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import world.travelgeeks.interfaces.adapter.TicketAdapter;

public class TicketManagement implements TicketAdapter {

    private final TicketAdapter ticketAdapter;

    public TicketManagement(TicketAdapter ticketAdapter) {
        this.ticketAdapter = ticketAdapter;
    }
    @Override
    public void create(Guild guild, Member member, TextChannel channel) {
        this.ticketAdapter.create(guild, member, channel);
    }

    @Override
    public void delete(Guild guild, long userId) {
        this.ticketAdapter.delete(guild, userId);
    }

    @Override
    public Member getMember(Guild guild, TextChannel channel) {
        return this.ticketAdapter.getMember(guild, channel);
    }

    @Override
    public long getMemberById(Guild guild, TextChannel channel) {
        return this.ticketAdapter.getMemberById(guild, channel);
    }

    @Override
    public TextChannel getChannel(Guild guild, long memberID) {
        return this.ticketAdapter.getChannel(guild, memberID);
    }

    @Override
    public boolean hasTicket(Guild guild, long memberID) {
        return this.ticketAdapter.hasTicket(guild, memberID);
    }

    @Override
    public boolean isTicket(Guild guild, TextChannel channel) {
        return this.ticketAdapter.isTicket(guild, channel);
    }
}
