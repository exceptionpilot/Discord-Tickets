package world.travelgeeks.database.manager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import world.travelgeeks.interfaces.adapter.BanAdapter;

import java.sql.Timestamp;

public class BanManagement implements BanAdapter {

    private final BanAdapter banAdapter;

    public BanManagement(BanAdapter banAdapter) {
        this.banAdapter = banAdapter;
    }

    @Override
    public void ban(Guild guild, User user, Timestamp timestamp) {
        this.banAdapter.ban(guild, user, timestamp);
    }

    @Override
    public void unban(Guild guild, User user) {
        this.banAdapter.unban(guild, user);
    }

    @Override
    public boolean hasBan(Guild guild, User user) {
        return this.banAdapter.hasBan(guild, user);
    }

    @Override
    public Timestamp getExpire(Guild guild, User user) {
        return null;
    }

    @Override
    public boolean isExpired(Guild guild, User user) {
        return false;
    }
}
