package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Enrollment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentService {
    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final Path filePath;
    private final List<Enrollment> enrollments = new ArrayList<>();

    public EnrollmentService(CourseService courseService) {
        this(courseService, null, Path.of("data", "enrollments.csv"));
    }

    public EnrollmentService(CourseService courseService, Path filePath) {
        this(courseService, null, filePath);
    }

    public EnrollmentService(CourseService courseService, DepartmentService departmentService) {
        this(courseService, departmentService, Path.of("data", "enrollments.csv"));
    }

    EnrollmentService(CourseService courseService, DepartmentService departmentService, Path filePath) {
        this.courseService = courseService;
        this.departmentService = departmentService;
        this.filePath = filePath;
        load();
    }

    public boolean selectCourse(int studentId, int courseId) {
        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isEmpty()) {
            return false;
        }
        boolean alreadyExists = enrollments.stream()
                .anyMatch(e -> e.getStudentId() == studentId && e.getCourseId() == courseId);
        if (alreadyExists) {
            return false;
        }
        Course course = courseOpt.get();
        enrollments.add(new Enrollment(studentId, courseId, course.getTitle(), course.getFee(), false));
        save();
        return true;
    }

    public List<Enrollment> viewEnrollments(int studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId() == studentId)
                .map(e -> new Enrollment(e.getStudentId(), e.getCourseId(), e.getCourseTitle(), e.getFee(), e.isPaid()))
                .toList();
    }

    public double pendingFees(int studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId() == studentId && !e.isPaid())
                .mapToDouble(Enrollment::getFee)
                .sum();
    }

    public double payAllPendingFees(int studentId) {
        double total = 0.0;
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getStudentId() == studentId && !enrollment.isPaid()) {
                enrollment.setPaid(true);
                total += enrollment.getFee();
            }
        }
        if (total > 0.0) {
            save();
        }
        return total;
    }

    public int selectDepartment(int studentId, String departmentName) {
        if (departmentService == null || departmentName == null || departmentName.isBlank()) {
            return 0;
        }

        int added = 0;
        for (Course course : departmentService.getCoursesForDepartment(departmentName)) {
            if (selectCourse(studentId, course.getCourseId())) {
                added++;
            }
        }
        return added;
    }

    public int removeEnrollmentsByStudent(int studentId) {
        int before = enrollments.size();
        enrollments.removeIf(e -> e.getStudentId() == studentId);
        int after = enrollments.size();
        if (after != before) {
            save();
        }
        return before - after;
    }

    public int removeEnrollmentsByCourse(int courseId) {
        int before = enrollments.size();
        enrollments.removeIf(e -> e.getCourseId() == courseId);
        int after = enrollments.size();
        if (after != before) {
            save();
        }
        return before - after;
    }

    public boolean deleteEnrollment(int studentId, int courseId) {
        boolean removed = enrollments.removeIf(e -> e.getStudentId() == studentId && e.getCourseId() == courseId);
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
                String[] parts = line.split(",", 5);
                enrollments.add(new Enrollment(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Boolean.parseBoolean(parts[4])
                ));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load enrollments", e);
        }
    }

    private void save() {
        List<String> lines = enrollments.stream()
                .map(e -> e.getStudentId() + "," + e.getCourseId() + "," + e.getCourseTitle() + "," + e.getFee() + "," + e.isPaid())
                .toList();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save enrollments", e);
        }
    }
}

