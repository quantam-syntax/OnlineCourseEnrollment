package org.example.onlinecourseenrollmentside;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.onlinecourseenrollmentside.model.Student;
import org.example.onlinecourseenrollmentside.service.CourseService;
import org.example.onlinecourseenrollmentside.service.DepartmentService;
import org.example.onlinecourseenrollmentside.service.EnrollmentService;
import org.example.onlinecourseenrollmentside.service.FeesService;
import org.example.onlinecourseenrollmentside.service.InstructorService;
import org.example.onlinecourseenrollmentside.service.StudentService;

import java.io.IOException;
import java.net.URL;

public class App extends Application {
    public static String currentRole;
    public static Student currentStudent;

    public static final StudentService studentService = new StudentService();
    public static final InstructorService instructorService = new InstructorService();
    public static final CourseService courseService = new CourseService();
    public static final DepartmentService departmentService = new DepartmentService(courseService, instructorService);
    public static final EnrollmentService enrollmentService = new EnrollmentService(courseService, departmentService);
    public static final FeesService feesService = new FeesService(enrollmentService);

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showScene("login.fxml", "Course Enrollment Login");
        primaryStage.show();
    }

    public static void showScene(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/onlinecourseenrollmentside/" + fxmlFile));
        Parent root = loader.load();
        primaryStage.setTitle(title);
        Scene scene = new Scene(root);
        URL stylesheet = App.class.getResource("/org/example/onlinecourseenrollmentside/style.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
        }
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

