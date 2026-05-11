package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Instructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstructorService {
    private final Path filePath;
    private final List<Instructor> instructors = new ArrayList<>();

    public InstructorService() {
        this(Path.of("data", "instructors.csv"));
    }

    InstructorService(Path filePath) {
        this.filePath = filePath;
        load();
    }

    public List<Instructor> getInstructors() {
        return new ArrayList<>(instructors);
    }

    public Optional<Instructor> findById(int id) {
        return instructors.stream().filter(i -> i.getInstructorId() == id).findFirst();
    }

    public boolean addInstructor(Instructor instructor) {
        if (findById(instructor.getInstructorId()).isPresent()) {
            return false;
        }
        instructors.add(instructor);
        save();
        return true;
    }

    public boolean deleteInstructor(int instructorId) {
        boolean removed = instructors.removeIf(i -> i.getInstructorId() == instructorId);
        if (removed) {
            save();
        }
        return removed;
    }

    private void load() {
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                return;
            }
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", 2);
                if (parts.length < 2) {
                    continue;
                }
                instructors.add(new Instructor(Integer.parseInt(parts[0]), parts[1]));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load instructors", e);
        }
    }

    private void save() {
        List<String> lines = instructors.stream()
                .map(i -> i.getInstructorId() + "," + i.getName())
                .toList();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save instructors", e);
        }
    }
}


