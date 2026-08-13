package it.uniroma2.dicii.ispw.sostudy.application;

import org.json.JSONArray;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONHelper {
    private JSONHelper() {}

    public static JSONArray readJsonFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            return new JSONArray();
        }

        String content = Files.readString(path);

        if (content.isBlank()) {
            return new JSONArray();
        }

        return new JSONArray(content);
    }

    public static void writeJsonFile(String filePath, JSONArray jsonArray) throws IOException {
        Path path = Paths.get(filePath);
        Files.write(path, jsonArray.toString(4).getBytes());
    }
}
