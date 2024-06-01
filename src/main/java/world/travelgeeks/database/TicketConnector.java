package world.travelgeeks.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.interfaces.adapter.TicketAdapter;

import java.sql.*;

public class TicketConnector implements TicketAdapter {

    Logger logger = LoggerFactory.getLogger(TicketConnector.class);
    Connection connection;

    public TicketConnector(Connection connection) {
        this.connection = connection;
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Tickets(" +
                    "guildID BIGINT(22), " +
                    "memberID BIGINT(22), " +
                    "channelID BIGINT(22))");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            logger.debug("Reconnected to Database!");
        }
    }


    @Override
    public void create(Guild guild, Member member, TextChannel channel) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("insert into Tickets values(?,?,?)");
            statement.setLong(1, guild.getIdLong());
            statement.setLong(2, member.getIdLong());
            statement.setLong(3, channel.getIdLong());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(Guild guild, long userId) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("DELETE FROM Tickets WHERE guildID='" + guild.getIdLong() + "' AND memberID='" + userId + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            logger.debug("AUTO-FIX -> Reconnected to Database!");
        }
    }

    @Override
    public Member getMember(Guild guild, TextChannel channel) {
        Member member = null;
        try {
            Statement select = this.connection.createStatement();
            ResultSet resultSet = select.executeQuery("SELECT * FROM Tickets WHERE guildID='" + guild.getIdLong() + "' AND channelID='" + channel.getIdLong() + "'");
            if (!resultSet.next() || resultSet.getLong("memberID") == 0)  return null;
            member = guild.getMemberById(resultSet.getLong("memberID"));
            System.out.println(member);
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return member;
        }
    }

    @Override
    public long getMemberById(Guild guild, TextChannel channel) {
        long memberId = 0;
        try {
            Statement select = this.connection.createStatement();
            ResultSet resultSet = select.executeQuery("SELECT * FROM Tickets WHERE guildID='" + guild.getIdLong() + "' AND channelID='" + channel.getIdLong() + "'");
            if (!resultSet.next() || resultSet.getLong("memberID") == 0)  return 0;
            memberId = resultSet.getLong("memberID");
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return memberId;
        }
    }

    @Override
    public TextChannel getChannel(Guild guild, long memberID) {
        TextChannel channel = null;
        try {
            PreparedStatement select = this.connection.prepareStatement("SELECT * FROM Tickets WHERE guildID='" + guild.getIdLong() + "' AND memberID='" + memberID + "'");
            ResultSet resultSet = select.executeQuery();
            if (!resultSet.next() || resultSet.getLong("channelID") == 0) return null;
            channel = guild.getTextChannelById(resultSet.getLong("channelID"));
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return channel;
        }
    }

    @Override
    public boolean hasTicket(Guild guild, long memberID) {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Tickets WHERE guildID='" + guild.getIdLong() + "' AND memberID='" + memberID + "'");
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isTicket(Guild guild, TextChannel channel) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM Tickets WHERE guildID='" + guild.getIdLong() + "' AND channelID='" + channel.getIdLong() + "'");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return (resultSet.getLong("memberID") != 0);
            resultSet.close();
            return false;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
