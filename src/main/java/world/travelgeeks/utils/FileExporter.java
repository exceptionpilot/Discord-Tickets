package world.travelgeeks.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import world.travelgeeks.TicketBot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;

public class FileExporter {

    Logger logger = LoggerFactory.getLogger(FileExporter.class);

    public boolean exportResourceFile(String filename) throws IOException {
        Path path = Paths.get("./" + filename, new String[0]);

        if (!Files.exists(path, new LinkOption[0])) {
            if (TicketBot.class.getClassLoader().getResourceAsStream(filename) == null) return false;
            Files.copy(Objects.<InputStream>requireNonNull(TicketBot.class.getClassLoader().getResourceAsStream(filename)), path, new CopyOption[0]);
            logger.info("Export Resource: " + path);
            return true;
        }
        logger.info("Resource File is already existing at: " + path.toFile().getAbsolutePath());
        return false;
    }

    public boolean exportResourceFile(String filename, String destination) throws IOException {
        File folder = new File(destination);
        if (!folder.exists()) folder.mkdirs();

        Path path = Paths.get(destination + "/" + filename, new String[0]);

        if (!Files.exists(path, new LinkOption[0])) {
            if (TicketBot.class.getClassLoader().getResourceAsStream(filename) == null) return false;
            Files.copy(Objects.<InputStream>requireNonNull(TicketBot.class.getClassLoader().getResourceAsStream(filename)), path, new CopyOption[0]);
            logger.info("Export Resource: " + path);
            return true;
        }
        logger.info("Resource File is already existing at: " + path.toFile().getAbsolutePath());
        return false;
    }
}