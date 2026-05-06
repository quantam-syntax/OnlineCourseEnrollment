package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Student;
import org.example.onlinecourseenrollmentside.util.FileManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StudentService implements DataService<Student> {
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

    @Override
    public List<Student> getAll() {
        return new ArrayList<>(students);
    }

    @Override
    public Optional<Student> findById(int id) {
        return students.stream().filter(s -> s.getStudentId() == id).findFirst();
    }

    public boolean addStudent(Student student) {
        return add(student);
    }

    @Override
    public boolean add(Student item) {
        if (findById(item.getStudentId()).isPresent()) {
            return false;
        }
        students.add(item);
        save();
        return true;
    }

    public boolean deleteStudent(int studentId) {
        return delete(studentId);
    }

    @Override
    public boolean delete(int id) {
        boolean removed = students.removeIf(s -> s.getStudentId() == id);
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
            students.add(new Student(Integer.parseInt(parts[0]), name));
        }
    }

    private void save() {
        List<String> lines = students.stream()
                .map(s -> s.getStudentId() + "," + s.getName())
                .toList();
        FileManager.writeCSV(filePath, lines);
    }
}
