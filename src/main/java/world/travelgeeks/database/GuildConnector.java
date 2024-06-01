package world.travelgeeks.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.interfaces.adapter.GuildAdapter;

import java.sql.*;

public class GuildConnector implements GuildAdapter {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    Connection connection;

    public GuildConnector(Connection connection) {
        this.connection = connection;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + "Guilds(" +
                            "guildID BIGINT(22), " +
                            "roleID BIGINT(22), " +
                            "logChannelID BIGINT(22)," +
                            "categoryID BIGINT(22)," +
                            "ticketCount BIGINT(22))");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public Role getRole(Guild guild) {
        Role role = null;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Guilds WHERE guildID='" + guild.getIdLong() + "'");
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next() || resultSet.getLong("roleID") == 0) return role;
            role = guild.getRoleById(resultSet.getLong("roleID"));
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            return role;
        }
    }

    @Override
    public Category getCategory(Guild guild) {
        Category category = null;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Guilds WHERE guildID='" + guild.getIdLong() + "'");
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next() || resultSet.getLong("categoryID") == 0) return category;
            category = guild.getCategoryById(resultSet.getLong("categoryID"));
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            return category;
        }
    }

    @Override
    public TextChannel getLogChannel(Guild guild) {
        TextChannel channel = null;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Guilds WHERE guildID='" + guild.getIdLong() + "'");
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next() || resultSet.getLong("logChannelID") == 0) return null;
            channel = guild.getTextChannelById(resultSet.getLong("logChannelID"));
            resultSet.close();

        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            return channel;
        }

    }

    @Override
    public long getTicketCount(Guild guild) {
        long ticketCount = 0;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Guilds WHERE guildID='" + guild.getIdLong() + "'");
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next() || resultSet.getLong("ticketCount") == 0) return ticketCount;
            ticketCount = resultSet.getLong("ticketCount");
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            return ticketCount;
        }
    }

    @Override
    public void create(Guild guild) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("insert into Guilds values(?,?,?,?,?)");
            statement.setLong(1, guild.getIdLong());
            statement.setLong(2, 0);
            statement.setLong(3, 0);
            statement.setLong(4, 0);
            statement.setLong(5, 0);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(Guild guild) {
        this.logger.debug("incoming delete request: " + guild.toString());
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("DELETE FROM Tickets WHERE guildID='" + guild.getIdLong() + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean exists(Guild guild) {
        this.logger.debug("incoming get request: " + guild.toString());
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Guilds WHERE guildID='" + guild.getIdLong() + "'");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return (resultSet.getLong("ticketCount") != -1);
            resultSet.close();
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void setRole(Guild guild, Role role) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Guilds SET roleID='" + role.getIdLong() + "' WHERE guildID='" + guild.getIdLong() + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setCategory(Guild guild, Category category) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Guilds SET categoryID='" + category.getIdLong() + "' WHERE guildID='" + guild.getIdLong() + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setLogChannel(Guild guild, TextChannel channel) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Guilds SET logChannelID='" + channel.getIdLong() + "' WHERE guildID='" + guild.getIdLong() + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setTicketCount(Guild guild, long l) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE Guilds SET ticketCount='" + l + "' WHERE guildID='" + guild.getIdLong() + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public long addTicketCount(Guild guild) {
        long count = this.getTicketCount(guild) + 1;
        this.setTicketCount(guild, count);
        return count;
    }
}