package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Instructor;
import org.example.onlinecourseenrollmentside.util.FileManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class InstructorService implements DataService<Instructor> {
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

    @Override
    public List<Instructor> getAll() {
        return new ArrayList<>(instructors);
    }

    @Override
    public Optional<Instructor> findById(int id) {
        return instructors.stream().filter(i -> i.getInstructorId() == id).findFirst();
    }

    public boolean addInstructor(Instructor instructor) {
        return add(instructor);
    }

    @Override
    public boolean add(Instructor item) {
        if (findById(item.getInstructorId()).isPresent()) {
            return false;
        }
        instructors.add(item);
        save();
        return true;
    }

    public boolean deleteInstructor(int instructorId) {
        return delete(instructorId);
    }

    @Override
    public boolean delete(int id) {
        boolean removed = instructors.removeIf(i -> i.getInstructorId() == id);
        if (removed) {
            save();
        }
        return removed;
    }

    private void load() {
        for (String[] parts : FileManager.readCSV(filePath)) {
            if (parts.length < 2) {
                continue;
            }
            String name = String.join(",", Arrays.copyOfRange(parts, 1, parts.length));
            instructors.add(new Instructor(Integer.parseInt(parts[0]), name));
        }
    }

    private void save() {
        List<String> lines = instructors.stream()
                .map(i -> i.getInstructorId() + "," + i.getName())
                .toList();
        FileManager.writeCSV(filePath, lines);
    }
}
