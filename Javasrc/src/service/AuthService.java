package service;

import model.User;
import repository.FileManager;
import java.util.ArrayList;

public class AuthService {
    private ArrayList<User> users;
    private FileManager fileManager;

    // Singleton pattern to keep track of the currently logged-in user
    private static User currentUser = null;

    public AuthService() {
        this.users = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private void loadFromFile() {
        ArrayList<String[]> data = fileManager.loadUsers();
        if (data.isEmpty()) {
            // Create default admin if no users exist
            User admin = new User("admin", "admin", "ADMIN", -1);
            users.add(admin);
            fileManager.saveUser(admin);
        } else {
            for (String[] parts : data) {
                if (parts.length == 4) {
                    try {
                        String username = parts[0].trim();
                        String password = parts[1].trim();
                        String role = parts[2].trim();
                        int studentId = Integer.parseInt(parts[3].trim());
                        users.add(new User(username, password, role, studentId));
                    } catch (NumberFormatException ex) {
                        System.out.println("Skipping malformed user row.");
                    }
                }
            }
        }
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean registerStudentUser(String username, String password, int studentId) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false; // Username already exists
            }
        }
        User newUser = new User(username, password, "STUDENT", studentId);
        users.add(newUser);
        fileManager.saveUser(newUser);
        return true;
    }
}
