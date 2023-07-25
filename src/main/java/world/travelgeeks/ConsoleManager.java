package world.travelgeeks;

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

        (new Thread(() -> { String line = "";

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            try {
                while ((line = reader.readLine()) != null) {
                    IConsole command;
                    System.out.print("{TICKET-BOT} CONSOLE ⋙ ");

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
