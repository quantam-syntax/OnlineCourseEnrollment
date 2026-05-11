package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Instructor;
import org.example.onlinecourseenrollmentside.model.Schedule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseService {
    private final Path filePath;
    private final List<Course> courses = new ArrayList<>();

    public CourseService() {
        this(Path.of("data", "courses.csv"));
    }

    CourseService(Path filePath) {
        this.filePath = filePath;
        load();
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public Optional<Course> findById(int id) {
        return courses.stream().filter(c -> c.getCourseId() == id).findFirst();
    }

    public boolean addCourse(Course course) {
        if (findById(course.getCourseId()).isPresent()) {
            return false;
        }
        courses.add(course);
        save();
        return true;
    }

    public boolean assignInstructorToCourse(int courseId, Instructor instructor) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.getCourseId() == courseId) {
                courses.set(i, new Course(course.getCourseId(), course.getTitle(), course.getFee(), instructor, course.getSchedule()));
                save();
                return true;
            }
        }
        return false;
    }

    public boolean deleteCourse(int courseId) {
        boolean removed = courses.removeIf(c -> c.getCourseId() == courseId);
        if (removed) {
            save();
        }
        return removed;
    }

    public boolean unassignInstructorFromCourses(int instructorId) {
        boolean changed = false;
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            Instructor instr = course.getInstructor();
            if (instr != null && instr.getInstructorId() == instructorId) {
                courses.set(i, new Course(course.getCourseId(), course.getTitle(), course.getFee(), null, course.getSchedule()));
                changed = true;
            }
        }
        if (changed) {
            save();
        }
        return changed;
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
                Instructor instructor = null;
                if (parts.length >= 5 && !parts[3].isBlank() && !parts[4].isBlank()) {
                    instructor = new Instructor(Integer.parseInt(parts[3]), parts[4]);
                }
                Schedule schedule = new Schedule();
                if (parts.length >= 7) {
                    schedule = new Schedule(parts[6], parts[5]);
                }
                courses.add(new Course(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]), instructor, schedule));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load courses", e);
        }
    }

    private void save() {
        List<String> lines = courses.stream()
                .map(c -> {
                    Instructor instructor = c.getInstructor();
                    String instructorId = instructor == null ? "" : String.valueOf(instructor.getInstructorId());
                    String instructorName = instructor == null ? "" : instructor.getName();
                    Schedule schedule = c.getSchedule();
                    String day = schedule == null ? "" : schedule.getDay();
                    String time = schedule == null ? "" : schedule.getTime();
                    return c.getCourseId() + "," + c.getTitle() + "," + c.getFee() + "," + instructorId + "," + instructorName + "," + day + "," + time;
                })
                .toList();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save courses", e);
        }
    }
}

