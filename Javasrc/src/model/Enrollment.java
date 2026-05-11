package model;

public class Enrollment {
    private int enrollmentId;
    private String enrollmentDate;
    private int studentId;
    private int courseId;

    public Enrollment(int enrollmentId , String enrollmentDate , int studentId , int courseId){
        this.enrollmentId = enrollmentId;
        this.enrollmentDate = enrollmentDate;
        this.studentId = studentId;
        this.courseId = courseId;
        }
    public int getEnrollmentId(){
        return enrollmentId;
    }
    public String getEnrollmentDate(){
        return enrollmentDate;
    }
    public int getStudentId(){
        return studentId;
    }
    public int getCourseId(){
        return courseId;
    }
    public boolean enrollCourse(){
        System.out.println("Enrolled in course ID: "+ courseId);
        return true;
    }
    public boolean dropCourse(){
        System.out.println("Dropped course ID: "+ courseId);
        return true;
    }
    @Override
    public String toString(){
        return enrollmentId + ","+enrollmentDate+","+ studentId+","+courseId;
    }
    
}
