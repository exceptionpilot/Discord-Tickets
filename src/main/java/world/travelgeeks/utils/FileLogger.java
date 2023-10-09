package world.travelgeeks.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger {

    private final boolean isDebug;

     enum Level {
         INFO,
         DEBUG,
         ERROR
    }

    public FileLogger(boolean isDebug) {

        this.isDebug = isDebug;
    }

    @Deprecated
    public boolean log(Level level, String string) {

         if (isDebug) level = Level.DEBUG;

        /**
         * @deprecated This methode is not outdated just not finished
         */

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(".n"))) {

        switch (level) {
            case INFO:
                writer.write(Level.INFO + " => " + string);
                writer.close();
            case DEBUG:
                writer.write(Level.DEBUG + " => " + string);
                writer.close();
            case ERROR:
                writer.write(Level.ERROR + " => " + string);
                writer.close();
        }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
