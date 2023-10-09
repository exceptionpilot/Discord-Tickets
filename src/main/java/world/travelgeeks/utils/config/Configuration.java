package world.travelgeeks.utils.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class Configuration {

    private boolean devMode;
    private String apiUri;
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
        this.devMode = this.get("devMode").getAsBoolean();
        this.host = this.get("mysql", "host").getAsString();
        this.user = this.get("mysql", "user").getAsString();
        this.table = this.get("mysql", "table").getAsString();
        this.password = this.get("mysql", "password").getAsString();
        this.port = this.get("mysql", "port").getAsLong();
        this.apiUri = this.get("transcript", "webpage").getAsString();

        this.isSFTPActive = this.get("sftp", "status").getAsBoolean();
        this.sftpPath = this.get("sftp", "path").getAsString();
        this.sftpHost = this.get("sftp", "host").getAsString();
        this.sftpPort = this.get("sftp", "port").getAsLong();
        this.sftpUser = this.get("sftp", "user").getAsString();
        this.sftpPassword = this.get("sftp", "password").getAsString();

    }

    /*
        <dependency>
            <groupId>com.googlecode.json-simple</groupId>
            <artifactId>json-simple</artifactId>
            <version>1.1.1</version>
        </dependency>
     */

    public boolean isDevMode() {
        return devMode;
    }

    public String getWebpageUrl() {
        return apiUri;
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
}
