package ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Student;
import model.User;
import service.AuthService;
import service.StudentService;

import java.io.IOException;

public class LoginController {

    @FXML private TextField loginUsernameField;
    @FXML private PasswordField loginPasswordField;

    @FXML private TextField signupNameField;
    @FXML private TextField signupDepartmentField;
    @FXML private TextField signupUsernameField;
    @FXML private PasswordField signupPasswordField;

    private AuthService authService;
    private StudentService studentService;

    @FXML
    public void initialize() {
        authService = new AuthService();
        studentService = new StudentService();
    }

    @FXML
    public void onLogin() {
        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required.");
            return;
        }

        User user = authService.login(username, password);
        if (user != null) {
            AuthService.setCurrentUser(user);
            openDashboard();
        } else {
            showError("Invalid username or password.");
        }
    }

    @FXML
    public void onSignUp() {
        String name = signupNameField.getText().trim();
        String dept = signupDepartmentField.getText().trim();
        String username = signupUsernameField.getText().trim();
        String password = signupPasswordField.getText().trim();

        if (name.isEmpty() || dept.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showError("All fields are required to sign up.");
            return;
        }

        // Register student to get ID
        Student student = studentService.registerStudent(name, dept);
        
        // Register user
        boolean success = authService.registerStudentUser(username, password, student.getStudentId());
        
        if (success) {
            showSuccess("Account created successfully! You can now log in.");
            signupNameField.clear();
            signupDepartmentField.clear();
            signupUsernameField.clear();
            signupPasswordField.clear();
        } else {
            showError("Username already exists.");
        }
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/dashboard.fxml"));
            Parent root = loader.load();
            
            // The DashboardController will configure itself based on AuthService.getCurrentUser()
            
            Stage stage = (Stage) loginUsernameField.getScene().getWindow();
            Scene scene = new Scene(root, 1100, 700);
            String stylesheet = getClass().getResource("/ui/view/style.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load dashboard: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
