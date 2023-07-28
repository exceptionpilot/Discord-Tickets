package world.travelgeeks.utils.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class Configuration {

    String apiLink;
    String host;
    String table;
    String user;
    String password;
    long port;

    JSONParser parser = new JSONParser();
    Logger logger = LoggerFactory.getLogger(Configuration.class);

    public Configuration() {
        this.apiLink = (String) parse("api");
        this.host = (String) parse("host");
        this.user = (String) parse("user");
        this.table = (String) parse("table");
        this.password = (String) parse("password");
        this.port = (long) parse("port");
    }

    public String getApiLink() {
        return apiLink;
    }

    public String getHost() {
        return this.host;
    }

    public String getTable() {
        return table;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public long getPort() {
        return this.port;
    }

    private Object parse(String path) {
        try (FileReader reader = new FileReader("config.json")) {
            JSONObject object = (JSONObject) parser.parse(reader);
            logger.debug("Parsing JSON: " + path);
            return object.get(path);
        } catch (IOException | ParseException exception) {
            throw new RuntimeException(exception);
        }
    }
}
