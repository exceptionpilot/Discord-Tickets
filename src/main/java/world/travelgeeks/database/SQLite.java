package world.travelgeeks.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.utils.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    Configuration configuration = TicketBot.getInstance().getConfiguration();
    private Connection connection;

    public SQLite() {
        if (!configuration.useExternalDatabase()) {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            this.connect();
        }else logger.info("SQLite is disabled");
    }

    private void connect() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        }catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Connection dissolved: " + configuration.getUser() + "@" + configuration.getHost());
            } catch (SQLException exception) {
                logger.error("Connection could not be resolved: " + configuration.getUser() + "@" + configuration.getHost());
                exception.printStackTrace();
            }
        }
    }

    public void reconnect() {
        disconnect();
        connect();
    }

    public Connection getConnection() {
        return connection;
    }
}
