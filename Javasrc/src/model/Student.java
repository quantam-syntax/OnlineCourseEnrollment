package model;
import interfaces.Enrollable;
import java.util.ArrayList;

public class Student extends Person implements Enrollable{
    private int studentId;
    private String department;
    private ArrayList<Integer> enrolledCourses;
    public Student(int studentId,int id, String name , String department ){
        super(id, name);
        this.studentId=studentId;
        this.department = department;
        this.enrolledCourses = new ArrayList<>();
    }
    public int getStudentId(){
        return studentId;
    }
    public String getDepartment(){
        return department;
    }
    public boolean registerStudent(){
        System.out.println("Student registered: " + getName());
        return true;
    }
    @Override
    public void enrollCourse(){
        System.out.println(getName() + " enrolled in a course");
    }
    @Override
    public void dropCourse() {
        System.out.println(getName() + " dropped a course.");
    }
    public ArrayList<Integer>viewCourses(){
        return enrolledCourses;
    }
    public void addCourseToList(int courseId){
    enrolledCourses.add(courseId);
}

@Override
public void displayInfo(){
    System.out.println("Student id : "+ studentId + " Name : "+ getName()+ " Department : "+ department);
}
}