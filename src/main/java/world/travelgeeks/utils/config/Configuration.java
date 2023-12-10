package world.travelgeeks.utils.config;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Configuration {

    private boolean devMode;
    private String apiUri;
    private boolean useExternalDatabase;
    private String host;
    private String table;
    private String user;
    private String password;
    private long port;
    private boolean isSFTPActive;
    private String sftpPath;
    private String sftpHost;
    private long sftpPort;
    private String sftpUser;
    private String sftpPassword;
    Logger logger = LoggerFactory.getLogger(Configuration.class);

    public Configuration() {

        FileReader config;

        try {
             config = new FileReader("config.json");
            this.devMode = this.fromFile(new FileReader("config.json"), "dev.debug").getAsBoolean();
            this.useExternalDatabase = this.fromFile(new FileReader("config.json"), "mysql.use").getAsBoolean();
            this.host = this.fromFile(new FileReader("config.json"), "mysql.host").getAsString();
            this.user = this.fromFile(new FileReader("config.json"), "mysql.user").getAsString();
            this.table = this.fromFile(new FileReader("config.json"), "mysql.table").getAsString();
            this.password = this.fromFile(new FileReader("config.json"), "mysql.password").getAsString();
            this.port = this.fromFile(new FileReader("config.json"), "mysql.port").getAsInt();
            this.apiUri = this.fromFile(new FileReader("config.json"), "transcript.uri").getAsString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //this.isSFTPActive = this.get("sftp", "status").getAsBoolean();
        //this.sftpPath = this.get("sftp", "path").getAsString();
        //this.sftpHost = this.get("sftp", "host").getAsString();
        //this.sftpPort = this.get("sftp", "port").getAsLong();
        //this.sftpUser = this.get("sftp", "user").getAsString();
        //this.sftpPassword = this.get("sftp", "password").getAsString();

    }
    // Recode configuration handling

    public boolean isDevMode() {
        return devMode;
    }

    public String getWebpageUrl() {
        return apiUri;
    }

    public boolean useExternalDatabase() {
        return this.useExternalDatabase;
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

    public boolean isSFTPActive() {
        return isSFTPActive;
    }

    public String getSftpPath() {
        return sftpPath;
    }

    public String getSftpHost() {
        return sftpHost;
    }

    public long getSftpPort() {
        return sftpPort;
    }

    public String getSftpUser() {
        return sftpUser;
    }

    public String getSftpPassword() {
        return sftpPassword;
    }

    private JsonElement get(String key) {

        try (FileReader reader = new FileReader("config.json")) {

            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            return jsonObject.get(key);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    private JsonElement get(String path, String key) {

        try (FileReader reader = new FileReader("config.json")) {

            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            return jsonObject.getAsJsonObject(path).get(key);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public JsonElement fromFile(@NotNull FileReader json, @NotNull String path) throws JsonSyntaxException {
        JsonObject object = new GsonBuilder().create().fromJson(json, JsonObject.class);
        String[] splits = path.split("\\.");
        for (String element : splits) {
            if (object != null) {
                System.out.println(object);
                JsonElement jsonElement = object.get(element);
                if (!jsonElement.isJsonObject()) {
                    return jsonElement;
                } else {
                    object = jsonElement.getAsJsonObject();
                }
            } else {
                System.out.println(object);
                return null;
            }
        }
        return object;
    }
}
