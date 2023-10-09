package world.travelgeeks.utils.config;

import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Test {

    private Map<String, Object> map;

    public Test() {

        try (FileReader fileReader = new FileReader("messages.json")) {
            Gson gson = new Gson();
            Map<?, ?> map = gson.fromJson(fileReader, Map.class);

            // print map entries
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public Object getMessage(String path) {
        return map.get(path);
    }

}
