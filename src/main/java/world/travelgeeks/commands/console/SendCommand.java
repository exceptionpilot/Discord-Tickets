package world.travelgeeks.commands.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.interfaces.IConsole;
import world.travelgeeks.utils.config.Configuration;

public class SendCommand implements IConsole {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    Configuration configuration = TicketBot.getInstance().getConfiguration();

    @Override
    public void execute(String[] args) {

        if (!configuration.isDevMode()) return;

        if (args.length < 4) return;


        String guildID = args[1];
        String channelID = args[2];

        String msg = "";
        for (int i = 3; i != args.length; i++){
            msg = msg + " " + args[i];
        }

        TicketBot.getInstance().getJDA().getGuildById(guildID).getTextChannelById(channelID).sendMessage(msg).queue();
        logger.info("[BOT]: " + msg);

    }
}
