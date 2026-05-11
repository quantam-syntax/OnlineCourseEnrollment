package model;

public class Instructor extends Person {
    private int instructorId;

    public Instructor(int id, String name, int instructorId) {
        super(id, name);
        this.instructorId = instructorId;
    }
    public int getInstructorId() {
        return instructorId;
    }
    public boolean assignCourse(Course course) {
        System.out.println("Course assigned by " + getName());
        return true;
    }
    @Override
    public void displayInfo() {
        System.out.println("Instructor ID: " + getInstructorId());
        System.out.println("Instructor Name: " + getName());

}
}