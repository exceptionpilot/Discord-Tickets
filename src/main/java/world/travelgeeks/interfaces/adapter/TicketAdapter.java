package world.travelgeeks.interfaces.adapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public interface TicketAdapter {

    void create(Guild guild, Member member, TextChannel channel);
    void delete(Guild guild, long userId);

    Member getMember(Guild guild, TextChannel channel);
    long getMemberById(Guild guild, TextChannel channel);
    TextChannel getChannel(Guild guild, long memberID);
    boolean hasTicket(Guild guild, long memberID);
    boolean isTicket(Guild guild, TextChannel channel);
}