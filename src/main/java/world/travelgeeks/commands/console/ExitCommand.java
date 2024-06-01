package world.travelgeeks.commands.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.MySQL;
import world.travelgeeks.interfaces.IConsole;

public class ExitCommand implements IConsole {
    TicketBot ticketBot = TicketBot.getInstance();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(String[] args) {
        logger.info("Shutdown");
        if (args.length == 0) {
            ticketBot.getJDA().shutdown();
            ticketBot.getJDA().shutdownNow();
            System.exit(Integer.valueOf(1));
            return;
        }

        ticketBot.getJDA().shutdown();
        ticketBot.getJDA().shutdownNow();
        System.exit(Integer.valueOf(args[1]));
    }
}
