package world.travelgeeks.interfaces.adapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public interface TicketAdapter {

    void create(Guild guild, Member member, TextChannel channel);
    void delete(Guild guild, Member member);

    Member getMember(Guild guild, TextChannel channel);
    TextChannel getChannel(Guild guild, Member member);
    boolean hasTicket(Guild guild, Member member);
    boolean isTicket(Guild guild, TextChannel channel);
}