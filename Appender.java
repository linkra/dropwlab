package dropwlab.delaval.com.core.vc.listener;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Appender {

    private static Path path = Paths.get("/src/main/event/vc-events.xml");

    public static void append(String xml) throws IOException {
        final File file = path.toFile();
        try (final Writer writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(xml);
        }
    }
}
