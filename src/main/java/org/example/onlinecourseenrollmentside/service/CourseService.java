package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Instructor;
import org.example.onlinecourseenrollmentside.model.Schedule;
import org.example.onlinecourseenrollmentside.util.FileManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CourseService implements DataService<Course> {
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

    @Override
    public List<Course> getAll() {
        return new ArrayList<>(courses);
    }

    @Override
    public Optional<Course> findById(int id) {
        return courses.stream().filter(c -> c.getCourseId() == id).findFirst();
    }

    public boolean addCourse(Course course) {
        return add(course);
    }

    @Override
    public boolean add(Course item) {
        if (findById(item.getCourseId()).isPresent()) {
            return false;
        }
        courses.add(item);
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
        return delete(courseId);
    }

    @Override
    public boolean delete(int id) {
        boolean removed = courses.removeIf(c -> c.getCourseId() == id);
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
        for (String[] parts : FileManager.readCSV(filePath)) {
            if (parts.length < 3) {
                continue;
            }
            if (parts.length >= 7) {
                int n = parts.length;
                String title = String.join(",", Arrays.copyOfRange(parts, 1, n - 5));
                double fee = Double.parseDouble(parts[n - 5]);
                Instructor instructor = null;
                if (!parts[n - 4].isBlank() && !parts[n - 3].isBlank()) {
                    instructor = new Instructor(Integer.parseInt(parts[n - 4]), parts[n - 3]);
                }
                Schedule schedule = new Schedule(parts[n - 1], parts[n - 2]);
                courses.add(new Course(Integer.parseInt(parts[0]), title, fee, instructor, schedule));
            } else {
                Instructor instructor = null;
                if (parts.length >= 5 && !parts[3].isBlank() && !parts[4].isBlank()) {
                    instructor = new Instructor(Integer.parseInt(parts[3]), parts[4]);
                }
                Schedule schedule = new Schedule();
                courses.add(new Course(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]), instructor, schedule));
            }
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
        FileManager.writeCSV(filePath, lines);
    }
}
