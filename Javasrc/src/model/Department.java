package model;
import java.util.ArrayList;

public class Department{
    private int departmentId;
    private String departmentName;
    private ArrayList<Course> courses;
    private ArrayList<Student> students;
    public Department(int departmentId, String departmentName){
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.courses = new ArrayList<>();
        this.students = new ArrayList<>();
    }
    public int getDepartmentId(){
        return departmentId;    
    
    }
    public String getDepartmentName(){
        return departmentName;
    }   

    public boolean addCourse(Course course){
        courses.add(course);
        return true;
    }
    public ArrayList<Course> getCourses(){
        return courses;
    }
    public void addStudent(Student student){
        students.add(student);
    }
}