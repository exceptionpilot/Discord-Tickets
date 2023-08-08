package world.travelgeeks.database.manager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import world.travelgeeks.interfaces.adapter.LoggingAdapter;

public class LoggingManagement implements LoggingAdapter {

    private final LoggingAdapter loggingAdapter;

    public LoggingManagement(LoggingAdapter loggingAdapter) {
        this.loggingAdapter = loggingAdapter;
    }
    
    @Override
    public void create(Guild guild, Member member, Message message) {
        this.loggingAdapter.create(guild, member, message);
    }

    @Override
    public void delete(Guild guild, long userId) {
        this.loggingAdapter.delete(guild, userId);
    }

    @Override
    public Message getMessage(Guild guild, long userId) {
        return this.loggingAdapter.getMessage(guild, userId);
    }
}
