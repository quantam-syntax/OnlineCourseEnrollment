package service;

import model.Course;
import model.Instructor;
import repository.FileManager;
import java.util.ArrayList;

public class InstructorService {
    private ArrayList<Instructor> instructors;
    private FileManager fileManager;
    private int instructorCounter = 1;

    public InstructorService() {
        this.instructors = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }

    public void loadFromFile() {
        ArrayList<String[]> data = fileManager.loadInstructors();
        for (String[] parts : data) {
            if (parts.length != 2) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                instructors.add(new Instructor(id, parts[1].trim(), id));
                instructorCounter = Math.max(instructorCounter, id + 1);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping malformed instructor row: " + String.join(",", parts));
            }
        }
    }

    public Instructor addInstructor(String name) {
        Instructor instructor = new Instructor(instructorCounter, name, instructorCounter);
        instructorCounter++;
        instructors.add(instructor);
        fileManager.saveInstructor(instructor);
        System.out.println("Instructor added ID: " + instructor.getInstructorId());
        return instructor;
    }

    public Instructor getInstructorById(int instructorId) {
        for (Instructor i : instructors) {
            if (i.getInstructorId() == instructorId) {
                return i;
            }
        }
        return null;
    }

    public boolean assignCourseToInstructor(int instructorId, Course course) {
        Instructor instructor = getInstructorById(instructorId);
        if (instructor != null) {
            instructor.assignCourse(course);
            return true;
        }
        System.out.println("Instructor not found");
        return false;
    }

    public void displayAllInstructors() {
        if (instructors.isEmpty()) {
            System.out.println("No instructors available");
            return;
        }
        for (Instructor i : instructors) {
            i.displayInfo();
        }
    }

    public ArrayList<Instructor> getInstructors() {
        return instructors;
    }
}