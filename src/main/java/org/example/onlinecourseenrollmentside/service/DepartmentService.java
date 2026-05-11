package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Department;
import org.example.onlinecourseenrollmentside.model.Instructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DepartmentService {
    private final CourseService courseService;
    private final InstructorService instructorService;
    private final Path filePath;
    private final List<Department> departments = new ArrayList<>();

    public DepartmentService(CourseService courseService, InstructorService instructorService) {
        this(courseService, instructorService, Path.of("data", "departments.csv"));
    }

    DepartmentService(CourseService courseService, InstructorService instructorService, Path filePath) {
        this.courseService = courseService;
        this.instructorService = instructorService;
        this.filePath = filePath;
        load();
    }

    public List<Department> getDepartments() {
        return new ArrayList<>(departments);
    }

    public Optional<Department> findByName(String name) {
        return departments.stream().filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
    }

    public boolean addDepartment(String name) {
        if (name == null || name.isBlank() || findByName(name).isPresent()) {
            return false;
        }
        departments.add(new Department(name.trim()));
        save();
        return true;
    }

    public boolean addCourseToDepartment(String departmentName, int courseId) {
        Optional<Department> departmentOpt = findByName(departmentName);
        if (departmentOpt.isEmpty() || courseService.findById(courseId).isEmpty()) {
            return false;
        }
        Department department = departmentOpt.get();
        boolean added = department.addCourseId(courseId);
        if (added) {
            save();
        }
        return added;
    }

    public boolean addInstructorToDepartment(String departmentName, int instructorId) {
        Optional<Department> departmentOpt = findByName(departmentName);
        if (departmentOpt.isEmpty() || instructorService.findById(instructorId).isEmpty()) {
            return false;
        }
        Department department = departmentOpt.get();
        boolean added = department.addInstructorId(instructorId);
        if (added) {
            save();
        }
        return added;
    }

    public boolean deleteDepartment(String name) {
        boolean removed = departments.removeIf(d -> d.getName().equalsIgnoreCase(name));
        if (removed) {
            save();
        }
        return removed;
    }

    public boolean removeCourseFromDepartments(int courseId) {
        boolean changed = false;
        for (Department d : departments) {
            if (d.removeCourseId(courseId)) {
                changed = true;
            }
        }
        if (changed) {
            save();
        }
        return changed;
    }

    public boolean removeInstructorFromDepartments(int instructorId) {
        boolean changed = false;
        for (Department d : departments) {
            if (d.removeInstructorId(instructorId)) {
                changed = true;
            }
        }
        if (changed) {
            save();
        }
        return changed;
    }

    public List<Course> getCoursesForDepartment(String departmentName) {
        return findByName(departmentName)
                .map(department -> {
                    List<Integer> instructorIds = department.getInstructorIds();
                    return courseService.getCourses().stream()
                            .filter(course -> course.getInstructor() != null)
                            .filter(course -> instructorIds.contains(course.getInstructor().getInstructorId()))
                            .collect(Collectors.toList());
                })
                .orElseGet(ArrayList::new);
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
                String[] parts = line.split(",", -1);
                if (parts.length < 3) {
                    continue;
                }
                departments.add(new Department(
                        parts[0],
                        parseIds(parts[1]),
                        parseIds(parts[2])
                ));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load departments", e);
        }
    }

    private List<Integer> parseIds(String raw) {
        List<Integer> ids = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return ids;
        }
        for (String value : raw.split(";")) {
            if (!value.isBlank()) {
                ids.add(Integer.parseInt(value.trim()));
            }
        }
        return ids;
    }

    private String joinIds(List<Integer> ids) {
        return ids.stream().map(String::valueOf).collect(Collectors.joining(";"));
    }

    private void save() {
        List<String> lines = departments.stream()
                .map(d -> d.getName() + "," + joinIds(d.getCourseIds()) + "," + joinIds(d.getInstructorIds()))
                .toList();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save departments", e);
        }
    }
}


