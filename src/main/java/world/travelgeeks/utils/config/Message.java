package world.travelgeeks.utils.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Message {

    String lang;
    String permission;
    String onlyGuild;
    String category;
    JSONParser parser = new JSONParser();

    public Message() {
        this.permission = (String) parse("$.error.permission");
        this.onlyGuild = (String) parse("$.error.private");
        this.category = (String) parse("$.error.category");

    }

    public String getPermission() {
        return permission;
    }

    public String getOnlyGuild() {
        return onlyGuild;
    }

    public String getCategory() {
        return category;
    }

    private Object parse(String path) {
        try (FileReader reader = new FileReader("config.json")) {
            JSONObject object = (JSONObject) parser.parse(reader);
            return object.get(path);
        } catch (IOException | ParseException exception) {
            throw new RuntimeException(exception);
        }
    }
}