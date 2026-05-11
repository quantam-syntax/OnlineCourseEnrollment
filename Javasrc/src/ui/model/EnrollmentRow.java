package ui.model;

public class EnrollmentRow {
    private final int enrollmentId;
    private final int studentId;
    private final String studentName;
    private final int courseId;
    private final String courseName;
    private final String enrollmentDate;

    public EnrollmentRow(int enrollmentId,
                         int studentId,
                         String studentName,
                         int courseId,
                         String courseName,
                         String enrollmentDate) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.enrollmentDate = enrollmentDate;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }
}

