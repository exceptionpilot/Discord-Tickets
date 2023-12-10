package world.travelgeeks.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.BanManagement;
import world.travelgeeks.interfaces.adapter.BanAdapter;

import java.sql.*;
import java.time.LocalDateTime;

public class BanConnector implements BanAdapter {

    public final Connection connection;
    BanManagement banManagement = TicketBot.getInstance().getBanManagement();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public BanConnector(Connection connection) {
        this.connection = connection;

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + "Ban(" +
                            "guildID BIGINT(22), " +
                            "userID BIGINT(22)," +
                            "expiring TIMESTAMP)"
            );
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void ban(Guild guild, User user, Timestamp timestamp) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("insert into Ban values(?,?,?)");
            statement.setLong(1, guild.getIdLong());
            statement.setLong(2, user.getIdLong());
            statement.setTimestamp(3, timestamp);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void unban(Guild guild, User user) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("DELETE FROM Ban WHERE guildID='" + guild.getIdLong() + "' AND userID='" + user.getIdLong() + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            logger.debug("Reconnected to Database!");
        }
    }

    @Override
    public boolean hasBan(Guild guild, User user) {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Ban WHERE guildID='" + guild.getIdLong() + "' AND userID='" + user.getIdLong() + "'");
            if (resultSet.next()) {
                if (this.isExpired(guild, user)) {
                    this.unban(guild, user);
                    return false;
                } else return true;
            } else return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Timestamp getExpire(Guild guild, User user) {
        Timestamp expiring = null;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ban WHERE guildID='" + guild.getIdLong() + "' AND userID='" + user.getIdLong() + "'");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                expiring = resultSet.getTimestamp("expiring");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        } finally {
            return expiring;
        }
    }

    @Override
    public boolean isExpired(Guild guild, User user) {
        return this.getExpire(guild, user).toLocalDateTime().isBefore(LocalDateTime.now());
    }
}
