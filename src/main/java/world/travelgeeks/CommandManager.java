package world.travelgeeks;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import world.travelgeeks.commands.*;
import world.travelgeeks.interfaces.ICommand;
import world.travelgeeks.utils.config.Messages;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager extends ListenerAdapter {

    private Map<String, ICommand> commandsMap;
    private JDA jda;

    public CommandManager() {
        jda = TicketBot.getInstance().getJDA();

        commandsMap = new ConcurrentHashMap<>();

        commandsMap.put("config", new ConfigCommand());
        commandsMap.put("help", new HelpCommand());
        commandsMap.put("topic", new TopicCommand());
        commandsMap.put("open", new OpenCommand());
        commandsMap.put("close", new CloseCommand());
        commandsMap.put("add", new AddCommand());
        commandsMap.put("remove", new RemoveCommand());
        commandsMap.put("claim", new ClaimCommand());
        commandsMap.put("ticket", new TicketCommand());

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(Commands.slash("help", "Get the help page for the " + jda.getSelfUser().getName() + "!"));
        commands.addCommands(Commands.slash("claim", "Claim ticket!"));
        commands.addCommands(Commands.slash("open", "Open a new Ticket!")
                .addOptions(new OptionData(OptionType.USER, "member", "Choose a member!")));
        commands.addCommands(Commands.slash("close", "Close the ticket!"));
        commands.addCommands(Commands.slash("add", "Manage your Ticket")
                .addOptions(new OptionData(OptionType.USER, "member", "Choose a member!", true)));
        commands.addCommands(Commands.slash("remove", "Manage your Ticket")
                .addOptions(new OptionData(OptionType.USER, "member", "Choose a member!", true)));
        commands.addCommands(Commands.slash("config", "Only use this command for first setup or reset!")
                .addOptions(new OptionData(OptionType.ROLE, "role", "Choose a your moderation role!", true))
                .addOptions(new OptionData(OptionType.CHANNEL, "channel", "Select a channel for logging!", true))
                .addOptions(new OptionData(OptionType.CHANNEL, "category", "Choose a category for your tickets.")));
        commands.addCommands(Commands.slash("topic", "Change the ticket topic")
                .addOptions(new OptionData(OptionType.STRING, "topic", "Change the ticket topic", true)));
        commands.addCommands(Commands.slash("ticket", "Ticket management.")
                .addSubcommands(new SubcommandData("ban", "Ban a ticket User")
                                .addOptions(new OptionData(OptionType.USER, "user", "Select a user", true)),
                        new SubcommandData("unban", "Unban a ticket user.")
                                .addOptions(new OptionData(OptionType.USER, "user", "Select a user", true))
                ));

        // Context Commands
        commands.addCommands(Commands.context(Command.Type.USER, "Open Ticket"));

        commands.queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String commandName = event.getName();

        ICommand command;

        if ((command = commandsMap.get(commandName)) != null) {
            command.execute(event, event.getUser(), TicketBot.getInstance().getMessage());
        }

    }
}