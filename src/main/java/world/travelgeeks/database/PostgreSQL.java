package world.travelgeeks.database;

import sun.security.krb5.internal.Ticket;
import world.travelgeeks.TicketBot;
import world.travelgeeks.utils.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQL {

    private final Configuration configuration = TicketBot.getInstance().getConfiguration();
    Connection connection;

    private void connect() {
        this.connection = DriverManager.getConnection(configuration);
    }
}
