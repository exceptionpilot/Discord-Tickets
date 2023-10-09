package world.travelgeeks;

import world.travelgeeks.commands.console.DebugCommand;
import world.travelgeeks.commands.console.ExitCommand;
import world.travelgeeks.commands.console.SendCommand;
import world.travelgeeks.interfaces.IConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsoleManager {

    private Map<String, IConsole> commandsMap;

    public ConsoleManager() {
        commandsMap = new ConcurrentHashMap<>();
        commandsMap.put("exit", new ExitCommand());
        commandsMap.put("debug", new DebugCommand());
        commandsMap.put("send", new SendCommand());

        (new Thread(() -> { String line = "";

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                while ((line = reader.readLine()) != null) {
                    IConsole command;

                    String[]args = line.split(" ");

                    if ((command = commandsMap.get(args[0])) != null) {
                        command.execute(args);
                    }
                    continue;
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        })).start();

    }
}