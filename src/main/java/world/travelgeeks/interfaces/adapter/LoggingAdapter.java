package world.travelgeeks.interfaces.adapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public interface LoggingAdapter {

    void create(Guild guild, Member member, Message message);
    void delete(Guild guild, long userId);
    Message getMessage(Guild guild, long userId);
}
