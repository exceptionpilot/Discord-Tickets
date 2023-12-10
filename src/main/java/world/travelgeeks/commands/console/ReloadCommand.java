package world.travelgeeks.commands.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.MySQL;
import world.travelgeeks.interfaces.IConsole;
import world.travelgeeks.utils.config.Messages;

public class ReloadCommand implements IConsole {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(String[] args) {

        logger.info("Executing reload...please wait");
        Messages messages = TicketBot.getInstance().getMessage();
        messages.unloadFile();
        logger.info("We did it! :)");

    }
}
