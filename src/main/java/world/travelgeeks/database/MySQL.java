package world.travelgeeks.database;

import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.utils.config.Configuration;

import java.sql.*;

public class MySQL {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MySQL.class);

    Configuration configuration = TicketBot.getInstance().getConfiguration();
    private Connection connection;
    private ResultSet resultSet;

    public MySQL() {

        if (configuration.getHost() !=null) {
            connect();
        } else logger.info("Host is null");
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + configuration.getHost() + ":" + configuration.getPort() + "/" + configuration.getTable() + "?autoReconnect=true", configuration.getUser(), configuration.getPassword());
            logger.info("Connected to MYSQL_SERVER!");
        } catch (SQLException exception) {
            logger.error("Error on connecting to MYSQL_SERVER!");
            exception.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Disconnected MYSQL_SERVER!");
            } catch (SQLException exception) {
                logger.error("Error on disconnecting MYSQL_SERVER!");
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