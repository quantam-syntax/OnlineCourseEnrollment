package model;

public class User {
    private String username;
    private String password;
    private String role; // "ADMIN" or "STUDENT"
    private int studentId; // -1 if ADMIN

    public User(String username, String password, String role, int studentId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getStudentId() {
        return studentId;
    }
}
