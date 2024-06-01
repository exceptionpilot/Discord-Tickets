package world.travelgeeks.utils.config;

import com.google.gson.*;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Messages {

    private Map<String, JsonElement> store;

    public Messages() {
        this.store = new ConcurrentHashMap<>();
    }

    public JsonElement getMessage(String path) {
        if (this.store.get(path) !=null) {
            return this.store.get(path);
        } else {
            try (FileReader fileReader = new FileReader("messages.json")) {
                JsonElement result = this.fromFile(fileReader, path);
                this.store.put(path, result);
                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<EmbedBuilder> getEmbed(@NotNull String path) {
        List<EmbedBuilder> embeds = new ArrayList<>();
        for (JsonElement element : this.getMessage(path).getAsJsonArray()) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setAuthor(this.fromString(element.getAsString(), path + ".author").getAsString());
            builder.setTitle(this.fromString(element.getAsString(), path + ".title").getAsString());
            builder.setDescription(this.fromString(element.getAsString(), path + ".description").getAsString());
            builder.setFooter(this.fromString(element.getAsString(), path + ".footer").getAsString());
            builder.setColor(Color.decode(this.fromString(element.getAsString(), path + ".color").getAsString()));
            builder.setThumbnail(this.fromString(element.getAsString(), path + ".thumbnail").getAsString());
            builder.setImage(this.fromString(element.getAsString(), path + ".image").getAsString());

            for (JsonElement array : element.getAsJsonArray()) {
                builder.addField(
                        this.fromString(array.getAsString(), path + ".fields.key").getAsString(),
                        this.fromString(array.getAsString(), path + ".fields.value").getAsString(),
                        this.fromString(array.getAsString(), path + ".fields.inline").getAsBoolean()
                );
            }
            embeds.add(builder);
        }
        return embeds;
    }

    public JsonElement fromFile(@NotNull FileReader json, @NotNull String path) throws JsonSyntaxException {
        JsonObject object = new GsonBuilder().create().fromJson(json, JsonObject.class);
        String[] splits = path.split("\\.");
        for (String element : splits) {
            if (object != null) {
                JsonElement jsonElement = object.get(element);
                if (!jsonElement.isJsonObject())
                    return jsonElement;
                else
                    object = jsonElement.getAsJsonObject();
            } else return null;
        }
        return object;
    }

    public JsonElement fromString(String json, String path) throws JsonSyntaxException {
        JsonObject object = new GsonBuilder().create().fromJson(json, JsonObject.class);
        String[] splits = path.split("\\.");
        for (String element : splits) {
            if (object != null) {
                JsonElement jsonElement = object.get(element);
                if (!jsonElement.isJsonObject())
                    return jsonElement;
                else
                    object = jsonElement.getAsJsonObject();
            } else {
                return null;
            }
        }
        return object;
    }

    public void unloadFile() {
        this.store.clear();
    }
}
