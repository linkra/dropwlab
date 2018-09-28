package dropwlab.delaval.com.core.vc.listener;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;
import java.util.stream.Stream;

class Appender {

    private static final Logger logger = Logger.getLogger(Appender.class.getName());

    static void append(String uglyXml) throws IOException, URISyntaxException {
        URL systemResource = ClassLoader.getSystemResource("vc-events.xml");
        if (systemResource != null) {
            Path path = Paths.get(systemResource.toURI());
            File file = path.toFile();
            try (final Writer writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write("\n" + uglyXml + "\n");
            }
            logger.info("Wrote to file");
        }
    }

    /** Paths can be passed as parameters as well */
    static void appendWellFormedXml(String outputXmlTmp, String outputXml) throws IOException {
        // Temporary file, with only one event, always overwritten
        String filePath = "C:\\development\\dropwlab-delaval\\eventFromVCTmp.xml";

        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        // Merged file
        File file = new File("C:\\development\\dropwlab-delaval\\eventsFromVC.xml");

        try (final Writer writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write("\n" + contentBuilder.toString() + "\n");
            logger.info("Wrote to file: " + file.getName());
        }
    }
}
