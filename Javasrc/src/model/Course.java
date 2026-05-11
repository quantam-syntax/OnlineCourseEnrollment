package model;

public class Course {
    private int courseId;
    private String courseName;
    private int credits;
    public Course(int courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }
    public int getCourseId(){
        return courseId;
    }
    public String getCourseName(){
        return courseName;
    }
    public int getCredits(){
        return credits;
    }
    public boolean addCourse() {
        System.out.println("Course added: " + courseName);
        return true;
    }
    public String displayCourse(){
        return "ID: "+ courseId + " Name: " + courseName + " Credits: " + credits;
    }
    
}
