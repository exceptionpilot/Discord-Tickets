package world.travelgeeks.interfaces.adapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public interface GuildAdapter {

    Role getRole(Guild guild);
    Category getCategory(Guild guild);
    TextChannel getLogChannel(Guild guild);
    long getTicketCount(Guild guild);

    void create(Guild guild);
    void delete(Guild guild);
    boolean exists(Guild guild);
    void setRole(Guild guild, Role role);
    void setCategory(Guild guild, Category category);
    void setLogChannel(Guild guild, TextChannel channel);
    void setTicketCount(Guild guild, long l);
    long addTicketCount(Guild guild);
}