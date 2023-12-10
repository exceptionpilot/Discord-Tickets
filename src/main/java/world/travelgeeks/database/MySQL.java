package world.travelgeeks.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.utils.config.Configuration;

import java.sql.*;

public class MySQL {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    Configuration configuration = TicketBot.getInstance().getConfiguration();
    private Connection connection;
    private ResultSet resultSet;

    public MySQL() {

        if (configuration.useExternalDatabase()) {
            connect();
        } else logger.info("MySQL disabled");
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + configuration.getHost() + ":" + configuration.getPort() + "/" + configuration.getTable() + "?autoReconnect=true", configuration.getUser(), configuration.getPassword());
            logger.info("Bound connection to " + configuration.getUser() + "@" + configuration.getHost() + ":" + configuration.getPort());
        } catch (SQLException exception) {
            logger.error("Could not connect to " + configuration.getUser() + "@" + configuration.getHost());
            exception.printStackTrace();
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