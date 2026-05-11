package service;

import model.Course;
import model.Department;
import model.Student;
import repository.FileManager;
import java.util.ArrayList;

public class DepartmentService {
    private ArrayList<Department> departments;
    private FileManager fileManager;
    private int departmentCounter = 1;

    public DepartmentService() {
        this.departments = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }

    public void loadFromFile() {
        ArrayList<String[]> data = fileManager.loadDepartments();
        for (String[] parts : data) {
            if (parts.length != 2) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                departments.add(new Department(id, parts[1].trim()));
                departmentCounter = Math.max(departmentCounter, id + 1);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping malformed department row: " + String.join(",", parts));
            }
        }
    }

    public Department addDepartment(String departmentName) {
        Department department = new Department(departmentCounter, departmentName);
        departmentCounter++;
        departments.add(department);
        fileManager.saveDepartment(department);
        System.out.println("Department added ID: " + department.getDepartmentId());
        return department;
    }

    public Department getDepartmentById(int departmentId) {
        for (Department d : departments) {
            if (d.getDepartmentId() == departmentId) {
                return d;
            }
        }
        return null;
    }

    public boolean addCourseToDepartment(int departmentId, Course course) {
        Department department = getDepartmentById(departmentId);
        if (department != null) {
            department.addCourse(course);
            System.out.println("Course " + course.getCourseName() + " added to department ID: " + departmentId);
            return true;
        }
        System.out.println("Department not found");
        return false;
    }

    public boolean addStudentToDepartment(int departmentId, Student student) {
        Department department = getDepartmentById(departmentId);
        if (department != null) {
            department.addStudent(student);
            System.out.println("Student " + student.getName() + " added to department ID: " + departmentId);
            return true;
        }
        System.out.println("Department not found");
        return false;
    }

    public void viewCoursesInDepartment(int departmentId) {
        Department department = getDepartmentById(departmentId);
        if (department == null) {
            System.out.println("Department not found");
            return;
        }
        ArrayList<Course> courses = department.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses in this department");
            return;
        }
        System.out.println("Courses in " + department.getDepartmentName() + ":");
        for (Course c : courses) {
            System.out.println(c.displayCourse());
        }
    }

    public void displayAllDepartments() {
        if (departments.isEmpty()) {
            System.out.println("No departments available");
            return;
        }
        for (Department d : departments) {
            System.out.println("Department ID: " + d.getDepartmentId() + " Name: " + d.getDepartmentName());
        }
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }
}