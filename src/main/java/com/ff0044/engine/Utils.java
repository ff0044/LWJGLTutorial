package com.ff0044.engine;

import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.*;


public class Utils {
    private Utils() {

    }

    public static String readFile(String filePath) {
        String str;
        try {
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException excp) {
            Logger.error("Error reading file [" + filePath + "]", excp);
            throw new RuntimeException();
        }
        return str;
    }
}
