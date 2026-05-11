package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final Path filePath;
    private final List<Student> students = new ArrayList<>();

    public StudentService() {
        this(Path.of("data", "students.csv"));
    }

    StudentService(Path filePath) {
        this.filePath = filePath;
        load();
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Optional<Student> findById(int id) {
        return students.stream().filter(s -> s.getStudentId() == id).findFirst();
    }

    public boolean addStudent(Student student) {
        if (findById(student.getStudentId()).isPresent()) {
            return false;
        }
        students.add(student);
        save();
        return true;
    }

    public boolean deleteStudent(int studentId) {
        boolean removed = students.removeIf(s -> s.getStudentId() == studentId);
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
                students.add(new Student(Integer.parseInt(parts[0]), parts[1]));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load students", e);
        }
    }

    private void save() {
        List<String> lines = students.stream()
                .map(s -> s.getStudentId() + "," + s.getName())
                .toList();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save students", e);
        }
    }
}

