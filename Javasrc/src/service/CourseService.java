package service;
import model.Course;
import repository.FileManager;
import java.util.ArrayList;

public class CourseService {
    public ArrayList<Course> courses;
    private FileManager fileManager;
    private int courseCounter = 1;
    public CourseService(){
        this.courses = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }
    public void loadFromFile(){
        ArrayList<String[]> data = fileManager.loadCourses();
        for (String[] parts : data){
            if (parts.length != 3){
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                int credits = Integer.parseInt(parts[2].trim());
                courses.add(new Course(id, parts[1], credits));
                courseCounter = Math.max(courseCounter, id + 1);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping malformed course row: " + String.join(",", parts));
            }
        }
    }
    public boolean addCourse(String courseName, int credits){
        Course course = new Course (courseCounter++, courseName , credits);
        courses.add(course);
        fileManager.saveCourse(course);
        System.out.println("THe course has been added " + courseName);
        return true ;
    }
    public boolean checkAvailability(int courseId){
        for (Course c : courses){
            if (c.getCourseId() == courseId)
                return true;
        }
        return false;
    }
    public Course getCourseById(int courseId){
        for (Course c : courses){
            if (c.getCourseId() == courseId)
                return c;
        }
        return null;
    }
    public void viewCourses(){
        if (courses.isEmpty()){
            System.out.println("No course Available");
            return;
        }
        for (Course c : courses){
            System.out.println(c.displayCourse());
        }
    }
    public ArrayList<Course>getCourses(){
        return courses;
    }
}
