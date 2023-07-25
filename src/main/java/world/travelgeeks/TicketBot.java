package world.travelgeeks;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.commands.ConfigCommand;
import world.travelgeeks.database.GuildConnector;
import world.travelgeeks.database.MySQL;
import world.travelgeeks.database.TicketConnector;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.interfaces.adapter.GuildAdapter;
import world.travelgeeks.interfaces.adapter.TicketAdapter;
import world.travelgeeks.listeners.ButtonInteractionListener;
import world.travelgeeks.listeners.GuildJoinLeaveListener;
import world.travelgeeks.utils.TicketWrapper;
import world.travelgeeks.utils.config.Configuration;
import world.travelgeeks.utils.FileExporter;
import world.travelgeeks.utils.config.Message;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.SplittableRandom;

public class TicketBot {

    Logger logger = LoggerFactory.getLogger(TicketBot.class);
    private JDA jda;
    private Configuration configuration;
    private Message messages;
    private MySQL sql;
    private TicketConnector ticketConnector;
    private TicketManagement ticketManagement;
    private GuildConnector guildConnector;
    private GuildManagement guildManagement;
    private TicketWrapper ticketWrapper;

    private static TicketBot INSTANCE;
    public static void main(String[] args) {
        try {
            FileExporter fileExporter = new FileExporter();
            fileExporter.exportResourceFile(".env");
            fileExporter.exportResourceFile("config.json");
            fileExporter.exportResourceFile("database.json");
            fileExporter.exportResourceFile("style.css", "transcripts");

            new TicketBot();
        }catch (IOException | LoginException | IllegalArgumentException exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }

    public TicketBot() throws LoginException, IllegalArgumentException {
        INSTANCE = this;

        this.configuration = new Configuration();
        this.messages = new Message(Color.decode("#D0F7F4"));

        this.sql = new MySQL();
        this.ticketConnector = new TicketConnector((Connection) this.sql.getConnection());
        this.ticketManagement = new TicketManagement((TicketAdapter) this.ticketConnector);
        this.guildConnector = new GuildConnector((Connection) this.sql.getConnection());
        this.guildManagement = new GuildManagement((GuildAdapter) this.guildConnector);

        this.ticketWrapper = new TicketWrapper();




        // Building JDA Bot Client
        Dotenv env = Dotenv.load();
        JDABuilder builder = JDABuilder.createDefault(env.get("TOKEN"));

        builder.setActivity(Activity.watching("Support"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
        builder.setAutoReconnect(true);
        builder.disableCache(CacheFlag.ACTIVITY);
        builder.setMaxReconnectDelay(32);
        builder.addEventListeners(new ConfigCommand());
        builder.addEventListeners(new ButtonInteractionListener());
        builder.addEventListeners(new GuildJoinLeaveListener());

        jda = builder.build();
        jda.addEventListener(new CommandManager());

        logger.info("JDA Successfully build!");
        new ConsoleManager();

    }

    public static TicketBot getInstance() {
        return INSTANCE;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public TicketManagement getTicketManagement() {
        return ticketManagement;
    }

    public GuildManagement getGuildManagement() {
        return guildManagement;
    }

    public TicketWrapper getTicketWrapper() {
        return ticketWrapper;
    }

    public MySQL getSQL() {
        return sql;
    }

    public JDA getJDA() {
        return jda;
    }
}