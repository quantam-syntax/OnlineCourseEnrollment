package service;
import model.Course;
import model.Enrollment;
import repository.FileManager;
import java.util.ArrayList;
import java.time.LocalDate;

public class EnrollmentService {
    private ArrayList<Enrollment> enrollments;
    private FileManager fileManager;
    private CourseService courseService;
    private int enrollmentCounter = 1;

    public EnrollmentService(CourseService courseService){
        this.enrollments = new ArrayList<>();
        this.fileManager = new FileManager();
        this.courseService = courseService;
        loadFromFile();
    }
    private void loadFromFile(){
        ArrayList<String[]>data = fileManager.loadEnrollments();
        for (String[]parts : data){
            if (parts.length != 4){
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                int studentId = Integer.parseInt(parts[2].trim());
                int courseId = Integer.parseInt(parts[3].trim());
                enrollments.add(new Enrollment(id, parts[1], studentId, courseId));
                enrollmentCounter = Math.max(enrollmentCounter, id + 1);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping malformed enrollment row: " + String.join(",", parts));
            }
        }

    }
    public void selectCourse(int studentId,int courseId){
        boolean available = courseService.checkAvailability(courseId);
        if (available){
            Enrollment enrollment = new Enrollment(enrollmentCounter++,LocalDate.now().toString(),studentId,courseId);
            enrollments.add(enrollment);
            fileManager.saveEnrollment(enrollment);
            System.out.println("Enrollment confirmed ID : " + enrollment.getEnrollmentId());
        }else{
            System.out.println("Error : course not available");
        }

    
    }
    public boolean dropCourse(int enrollmentId){
        for (Enrollment e : enrollments){
            if (e.getEnrollmentId() == enrollmentId){
                enrollments.remove(e);
                fileManager.deleteEnrollment(enrollmentId);
                System.out.println("Course dropped ");
                return true;
            }
        }
        System.out.println("Enrollment not found");
        return false;
    }
    public void viewEnrollments(int studentId){
        boolean found = false;
        for (Enrollment e : enrollments ){
            if (e.getStudentId() == studentId){
                Course course = courseService.getCourseById(e.getCourseId());
                System.out.println("Enrollment ID : " + e.getEnrollmentId() + " Course : " + (course != null ? course.getCourseName() : e.getCourseId()) + "Date : " + e.getEnrollmentDate());
                found = true;
            }
        }
        if (!found){
            System.out.println("Enrollment not found ");

        }
        }
        public ArrayList<Enrollment>getEnrollments(){
            return enrollments;

        }
    
}
