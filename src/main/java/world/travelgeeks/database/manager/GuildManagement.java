package world.travelgeeks.database.manager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import world.travelgeeks.interfaces.adapter.GuildAdapter;

public class GuildManagement implements GuildAdapter {

    private final GuildAdapter guildAdapter;

    public GuildManagement(GuildAdapter guildAdapter) {
        this.guildAdapter = guildAdapter;
    }


    @Override
    public Role getRole(Guild guild) {
        return this.guildAdapter.getRole(guild);
    }

    @Override
    public Category getCategory(Guild guild) {
        return this.guildAdapter.getCategory(guild);
    }

    @Override
    public TextChannel getLogChannel(Guild guild) {
        return this.guildAdapter.getLogChannel(guild);
    }

    @Override
    public long getTicketCount(Guild guild) {
        return this.guildAdapter.getTicketCount(guild);
    }

    @Override
    public void create(Guild guild) {
        this.guildAdapter.create(guild);
    }

    @Override
    public void delete(Guild guild) {
        this.guildAdapter.delete(guild);
    }

    @Override
    public boolean exists(Guild guild) {
        return this.guildAdapter.exists(guild);
    }

    @Override
    public void setRole(Guild guild, Role role) {
        this.guildAdapter.setRole(guild, role);
    }

    @Override
    public void setCategory(Guild guild, Category category) {
        this.guildAdapter.setCategory(guild, category);
    }

    @Override
    public void setLogChannel(Guild guild, TextChannel channel) {
        this.guildAdapter.setLogChannel(guild, channel);
    }

    @Override
    public void setTicketCount(Guild guild, long l) {
        this.guildAdapter.setTicketCount(guild, l);
    }

    @Override
    public long addTicketCount(Guild guild) {
        return this.guildAdapter.addTicketCount(guild);
    }
}
