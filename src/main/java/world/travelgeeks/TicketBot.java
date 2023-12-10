/*
    ____  _  v.1.3.0                 __   _______      __        __
   / __ \(_)_____________  _________/ /  /_  __(_)____/ /_____  / /______
  / / / / / ___/ ___/ __ \/ ___/ __  /    / / / / ___/ //_/ _ \/ __/ ___/
 / /_/ / (__  ) /__/ /_/ / /  / /_/ /    / / / / /__/ ,< /  __/ /_(__  )
/_____/_/____/\___/\____/_/   \__,_/    /_/ /_/\___/_/|_|\___/\__/____/
 */

package world.travelgeeks;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.audio.SpeakingMode;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.commands.ConfigCommand;
import world.travelgeeks.database.*;
import world.travelgeeks.database.manager.BanManagement;
import world.travelgeeks.database.manager.GuildManagement;
import world.travelgeeks.database.manager.LoggingManagement;
import world.travelgeeks.database.manager.TicketManagement;
import world.travelgeeks.interfaces.adapter.BanAdapter;
import world.travelgeeks.interfaces.adapter.GuildAdapter;
import world.travelgeeks.interfaces.adapter.LoggingAdapter;
import world.travelgeeks.interfaces.adapter.TicketAdapter;
import world.travelgeeks.listeners.ButtonInteractionListener;
import world.travelgeeks.listeners.GuildJoinLeaveListener;
import world.travelgeeks.listeners.UserContextInteractionListener;
import world.travelgeeks.utils.TicketWrapper;
import world.travelgeeks.utils.config.Configuration;
import world.travelgeeks.utils.FileExporter;
import world.travelgeeks.utils.config.Messages;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.SecurityPermission;
import java.sql.Connection;

public class TicketBot {

    Logger logger = LoggerFactory.getLogger(TicketBot.class);
    private JDA jda;
    private final Configuration configuration;
    private final Messages messages;
    private final MySQL sql;
    private final SQLite sqLite;
    private final TicketConnector ticketConnector;
    private final TicketManagement ticketManagement;
    private final GuildConnector guildConnector;
    private final GuildManagement guildManagement;
    private final LoggingConnector loggingConnector;
    private final LoggingManagement loggingManagement;
    private final BanConnector banConnector;
    private final BanManagement banManagement;
    private final TicketWrapper ticketWrapper;
    private static TicketBot INSTANCE;

    public static void main(String[] args) {

        System.out.println(
                "    ____  _  v.1.3.0                 __   _______      __        __\n" +
                "   / __ \\(_)_____________  _________/ /  /_  __(_)____/ /_____  / /______\n" +
                "  / / / / / ___/ ___/ __ \\/ ___/ __  /    / / / / ___/ //_/ _ \\/ __/ ___/\n" +
                " / /_/ / (__  ) /__/ /_/ / /  / /_/ /    / / / / /__/ ,< /  __/ /_(__  )\n" +
                "/_____/_/____/\\___/\\____/_/   \\__,_/    /_/ /_/\\___/_/|_|\\___/\\__/____/\n" +
                "\n" +
                "          Copyright © 2023 Sebastian Zängler & Contributors\n" +
                "\n" +
                "Developer: @basti.ehz, @marcel113\n" +
                "GitHub: https://github.com/DevChewbacca/Java-Ticket-Bot \n"
        );

        try {

            FileExporter fileExporter = new FileExporter();
            fileExporter.exportResourceFile(".env");
            fileExporter.exportResourceFile("config.json");
            fileExporter.exportResourceFile("database.json");
            fileExporter.exportResourceFile("messages.json");
            fileExporter.exportResourceFile("style.css", "transcripts");
            new TicketBot();
        }catch (IOException | LoginException | IllegalArgumentException exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public TicketBot() throws LoginException, IllegalArgumentException {
        INSTANCE = this;

        Dotenv env = Dotenv.load();
        this.configuration = new Configuration();
        this.messages = new Messages();

        this.sql = new MySQL();
        this.sqLite = new SQLite();

        this.ticketConnector = new TicketConnector(this.configuration.useExternalDatabase() ? (Connection) this.sql.getConnection() : (Connection) this.sqLite.getConnection());
        this.ticketManagement = new TicketManagement((TicketAdapter) this.ticketConnector);
        this.guildConnector = new GuildConnector(this.configuration.useExternalDatabase() ? (Connection) this.sql.getConnection() : (Connection) this.sqLite.getConnection());
        this.guildManagement = new GuildManagement((GuildAdapter) this.guildConnector);
        this.loggingConnector = new LoggingConnector(this.configuration.useExternalDatabase() ? (Connection) this.sql.getConnection() : (Connection) this.sqLite.getConnection());
        this.loggingManagement = new LoggingManagement((LoggingAdapter) this.loggingConnector);
        this.banConnector = new BanConnector(this.configuration.useExternalDatabase() ? (Connection) this.sql.getConnection() : (Connection) this.sqLite.getConnection());
        this.banManagement = new BanManagement((BanAdapter) this.banConnector);

        this.ticketWrapper = new TicketWrapper();

        // Building JDA Bot Client
        JDABuilder builder = JDABuilder.createDefault(env.get(this.configuration.isDevMode() ? "DEV_TOKEN" : "TOKEN"));

        builder.setActivity(Activity.watching(this.configuration.isDevMode() ? "DevMode" : "Support"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES);
        builder.setAutoReconnect(true);
        builder.disableCache(CacheFlag.ACTIVITY);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL); // TODO: add caching management
        builder.setMaxReconnectDelay(32);
        builder.addEventListeners(new ConfigCommand());
        builder.addEventListeners(new ButtonInteractionListener());
        builder.addEventListeners(new GuildJoinLeaveListener());
        builder.addEventListeners(new UserContextInteractionListener());

        jda = builder.build();
        jda.addEventListener(new CommandManager());

        logger.info("Login as: " + this.jda.getSelfUser().getName() + "(" + this.jda.getSelfUser().getId() + ")");
        new ConsoleManager();

    }

    public static TicketBot getInstance() {
        return INSTANCE;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public Messages getMessage() {
        return messages;
    }

    public TicketManagement getTicketManagement() {
        return ticketManagement;
    }

    public GuildManagement getGuildManagement() {
        return guildManagement;
    }

    public LoggingManagement getLoggingManagement() {
        return loggingManagement;
    }

    public BanManagement getBanManagement() {
        return banManagement;
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