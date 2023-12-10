package world.travelgeeks.database;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.interfaces.adapter.LoggingAdapter;

import java.sql.*;

public class LoggingConnector implements LoggingAdapter {

    Connection connection;
    GuildManagement guildManagement = TicketBot.getInstance().getGuildManagement();
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public  LoggingConnector(Connection connection) {
        this.connection = connection;
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + "Logging(" +
                            "guildID BIGINT(22), " +
                            "memberID BIGINT(22)," +
                            "messageID BIGINT(22))"
            );
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void create(Guild guild, Member member, Message message) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("insert into Logging values(?,?,?)");
            statement.setLong(1, guild.getIdLong());
            statement.setLong(2, member.getIdLong());
            statement.setLong(3, message.getIdLong());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(Guild guild, long userId) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate("DELETE FROM Logging WHERE guildID='" + guild.getIdLong() + "' AND memberID='" + userId + "'");
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Message getMessage(Guild guild, long userId) {
        RestAction<Message> message = null;

        try {
            PreparedStatement select = this.connection.prepareStatement("SELECT * FROM Logging WHERE guildID='" + guild.getIdLong() + "' AND memberID='" + userId + "'");
            ResultSet resultSet = select.executeQuery();
            if (!resultSet.next() || resultSet.getLong("messageID") == 0) return null;
            TextChannel channel = guildManagement.getLogChannel(guild);
            message = channel.retrieveMessageById(resultSet.getLong("messageID"));
            // MessageHistory history = MessageHistory.getHistoryFromBeginning(channel).complete(false);
            //message = history.getMessageById(resultSet.getLong("messageID"));
            resultSet.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            return message.complete();
        }
    }
}
