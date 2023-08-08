package world.travelgeeks.commands.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.MySQL;
import world.travelgeeks.interfaces.IConsole;

public class ExitCommand implements IConsole {

    MySQL sql = TicketBot.getInstance().getSQL();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(String[] args) {

        if (args.length == 0) {
            sql.disconnect();
            logger.info("Shutdown");
            System.exit(Integer.valueOf(1));
            return;
        }

        sql.disconnect();
        logger.info("Shutdown");
        System.exit(Integer.valueOf(args[1]));
    }
}
