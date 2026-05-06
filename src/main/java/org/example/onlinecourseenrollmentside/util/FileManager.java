package org.example.onlinecourseenrollmentside.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FileManager {

    private FileManager() {
    }

    public static List<String[]> readCSV(Path filePath) {
        try {
            if (!Files.exists(filePath)) {
                Path parent = filePath.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.createFile(filePath);
            }
            return Files.readAllLines(filePath).stream()
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(",", -1))
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read CSV: " + filePath, e);
        }
    }

    public static void writeCSV(Path filePath, List<String> lines) {
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write CSV: " + filePath, e);
        }
    }
}
