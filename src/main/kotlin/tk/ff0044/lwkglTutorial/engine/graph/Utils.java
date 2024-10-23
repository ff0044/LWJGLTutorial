package tk.ff0044.lwkglTutorial.engine.graph;

import org.tinylog.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(fileName)) {
            assert in != null;
            try (Scanner scanner = new Scanner(in, StandardCharsets.UTF_8)) {
                result = scanner.useDelimiter("\\A").next();
            }
        } catch (Exception e) {
            Logger.error("An error occured while trying to load resource: " + e.getStackTrace(), e);
            throw e;
        }

        return result;
    }

}
