package world.travelgeeks.interfaces.adapter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.sql.Timestamp;

public interface BanAdapter {

    void ban(Guild guild, User user, Timestamp timestamp);
    void unban(Guild guild, User user);

    boolean hasBan(Guild guild, User user);
    Timestamp getExpire(Guild guild, User user);
    boolean isExpired(Guild guild, User user);
}
