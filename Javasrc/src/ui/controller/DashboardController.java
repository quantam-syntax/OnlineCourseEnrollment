package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Course;
import model.Department;
import model.Enrollment;
import model.Fees;
import model.Instructor;
import model.Schedule;
import model.Student;
import model.User;
import service.CourseService;
import service.DepartmentService;
import service.EnrollmentService;
import service.FeesService;
import service.InstructorService;
import service.ScheduleService;
import service.StudentService;
import service.AuthService;
import ui.model.EnrollmentRow;
import ui.model.FeeRow;

public class DashboardController {

    @FXML private TabPane mainTabPane;
    @FXML private Tab studentsTab;
    @FXML private Tab coursesTab;
    @FXML private Tab enrollmentsTab;
    @FXML private Tab instructorsTab;
    @FXML private Tab departmentsTab;
    @FXML private Tab schedulesTab;
    @FXML private Tab feesTab;
    @FXML private HBox courseAddBox;

    // Student Tab
    @FXML private TextField studentNameField;
    @FXML private TextField studentDepartmentField;
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, Integer> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, String> studentDepartmentColumn;

    // Course Tab
    @FXML private TextField courseNameField;
    @FXML private TextField courseCreditsField;
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, Integer> courseIdColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, Integer> courseCreditsColumn;

    // Enrollment Tab
    @FXML private ComboBox<Student> enrollmentStudentCombo;
    @FXML private ComboBox<Course> enrollmentCourseCombo;
    @FXML private TableView<EnrollmentRow> enrollmentsTable;
    @FXML private TableColumn<EnrollmentRow, Integer> enrollmentIdColumn;
    @FXML private TableColumn<EnrollmentRow, Integer> enrollmentStudentIdColumn;
    @FXML private TableColumn<EnrollmentRow, String> enrollmentStudentNameColumn;
    @FXML private TableColumn<EnrollmentRow, Integer> enrollmentCourseIdColumn;
    @FXML private TableColumn<EnrollmentRow, String> enrollmentCourseNameColumn;
    @FXML private TableColumn<EnrollmentRow, String> enrollmentDateColumn;

    // Instructor Tab
    @FXML private TextField instructorNameField;
    @FXML private ComboBox<Instructor> instructorAssignCombo;
    @FXML private ComboBox<Course> instructorCourseCombo;
    @FXML private TableView<Instructor> instructorsTable;
    @FXML private TableColumn<Instructor, Integer> instructorIdColumn;
    @FXML private TableColumn<Instructor, String> instructorNameColumn;

    // Department Tab
    @FXML private TextField departmentNameField;
    @FXML private ComboBox<Department> departmentAssignCombo;
    @FXML private ComboBox<Course> departmentCourseCombo;
    @FXML private TableView<Department> departmentsTable;
    @FXML private TableColumn<Department, Integer> departmentIdColumn;
    @FXML private TableColumn<Department, String> departmentNameColumn;

    // Schedule Tab
    @FXML private TextField scheduleDayField;
    @FXML private TextField scheduleTimeField;
    @FXML private TableView<Schedule> schedulesTable;
    @FXML private TableColumn<Schedule, Integer> scheduleIdColumn;
    @FXML private TableColumn<Schedule, String> scheduleDayColumn;
    @FXML private TableColumn<Schedule, String> scheduleTimeColumn;

    // Fees Tab
    @FXML private ComboBox<Student> feesStudentCombo;
    @FXML private TextField feesAmountField;
    @FXML private TextField feesDeadlineField;
    @FXML private TableView<FeeRow> feesTable;
    @FXML private TableColumn<FeeRow, Integer> feeIdColumn;
    @FXML private TableColumn<FeeRow, Integer> feeStudentIdColumn;
    @FXML private TableColumn<FeeRow, Double> feeAmountColumn;
    @FXML private TableColumn<FeeRow, String> feeDeadlineColumn;
    @FXML private TableColumn<FeeRow, String> feeStatusColumn;

    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private InstructorService instructorService;
    private DepartmentService departmentService;
    private ScheduleService scheduleService;
    private FeesService feesService;

    @FXML
    public void initialize() {
        studentService = new StudentService();
        courseService = new CourseService();
        enrollmentService = new EnrollmentService(courseService);
        instructorService = new InstructorService();
        departmentService = new DepartmentService();
        scheduleService = new ScheduleService();
        feesService = new FeesService();

        initializeTables();
        initializeComboBoxes();
        refreshAll();
        applyRBAC();
    }

    private void applyRBAC() {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser != null && "STUDENT".equals(currentUser.getRole())) {
            mainTabPane.getTabs().removeAll(studentsTab, instructorsTab, departmentsTab, schedulesTab, feesTab);
            if (courseAddBox != null) {
                courseAddBox.setVisible(false);
                courseAddBox.setManaged(false);
            }
            Student myStudent = studentService.getStudentById(currentUser.getStudentId());
            if (myStudent != null) {
                enrollmentStudentCombo.setValue(myStudent);
                enrollmentStudentCombo.setDisable(true);
            }
        }
    }

    private void initializeTables() {
        // Student
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        // Course
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));

        // Enrollment
        enrollmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));
        enrollmentStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        enrollmentStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        enrollmentCourseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        enrollmentCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        enrollmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));

        // Instructor
        instructorIdColumn.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        instructorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Department
        departmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("departmentId"));
        departmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));

        // Schedule
        scheduleIdColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
        scheduleDayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        scheduleTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Fees
        feeIdColumn.setCellValueFactory(new PropertyValueFactory<>("feeId"));
        feeStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        feeAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        feeDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        feeStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
    }

    private void initializeComboBoxes() {
        StringConverter<Student> studentConverter = new StringConverter<>() {
            @Override
            public String toString(Student student) {
                return student == null ? "" : student.getStudentId() + " - " + student.getName();
            }
            @Override
            public Student fromString(String s) { return null; }
        };
        enrollmentStudentCombo.setConverter(studentConverter);
        feesStudentCombo.setConverter(studentConverter);

        StringConverter<Course> courseConverter = new StringConverter<>() {
            @Override
            public String toString(Course course) {
                return course == null ? "" : course.getCourseId() + " - " + course.getCourseName();
            }
            @Override
            public Course fromString(String s) { return null; }
        };
        enrollmentCourseCombo.setConverter(courseConverter);
        instructorCourseCombo.setConverter(courseConverter);
        departmentCourseCombo.setConverter(courseConverter);

        instructorAssignCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Instructor i) {
                return i == null ? "" : i.getInstructorId() + " - " + i.getName();
            }
            @Override
            public Instructor fromString(String s) { return null; }
        });

        departmentAssignCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Department d) {
                return d == null ? "" : d.getDepartmentId() + " - " + d.getDepartmentName();
            }
            @Override
            public Department fromString(String s) { return null; }
        });
    }

    // --- Student Tab Actions ---
    @FXML
    public void onRegisterStudent() {
        String name = studentNameField.getText().trim();
        String department = studentDepartmentField.getText().trim();
        if (name.isEmpty() || department.isEmpty()) {
            showError("Both student name and department are required.");
            return;
        }
        studentService.registerStudent(name, department);
        studentNameField.clear();
        studentDepartmentField.clear();
        refreshStudents();
        refreshSelectors();
    }

    // --- Course Tab Actions ---
    @FXML
    public void onAddCourse() {
        String name = courseNameField.getText().trim();
        String creditsText = courseCreditsField.getText().trim();
        if (name.isEmpty() || creditsText.isEmpty()) {
            showError("Course name and credits are required.");
            return;
        }
        try {
            int credits = Integer.parseInt(creditsText);
            if (credits <= 0) {
                showError("Credits must be greater than 0.");
                return;
            }
            courseService.addCourse(name, credits);
            courseNameField.clear();
            courseCreditsField.clear();
            refreshCourses();
            refreshSelectors();
        } catch (NumberFormatException ex) {
            showError("Credits must be a valid number.");
        }
    }

    // --- Enrollment Tab Actions ---
    @FXML
    public void onEnrollStudent() {
        Student selectedStudent = enrollmentStudentCombo.getValue();
        Course selectedCourse = enrollmentCourseCombo.getValue();
        if (selectedStudent == null || selectedCourse == null) {
            showError("Please select both a student and a course.");
            return;
        }
        enrollmentService.selectCourse(selectedStudent.getStudentId(), selectedCourse.getCourseId());
        refreshEnrollments();
        showSuccess("Successfully enrolled " + selectedStudent.getName() + " in " + selectedCourse.getCourseName());
    }

    @FXML
    public void onDropEnrollment() {
        EnrollmentRow selected = enrollmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an enrollment row to drop.");
            return;
        }
        enrollmentService.dropCourse(selected.getEnrollmentId());
        refreshEnrollments();
        showSuccess("Successfully dropped the enrollment.");
    }

    // --- Instructor Tab Actions ---
    @FXML
    public void onAddInstructor() {
        String name = instructorNameField.getText().trim();
        if (name.isEmpty()) {
            showError("Instructor name is required.");
            return;
        }
        instructorService.addInstructor(name);
        instructorNameField.clear();
        refreshInstructors();
        refreshSelectors();
    }

    @FXML
    public void onAssignCourseToInstructor() {
        Instructor instructor = instructorAssignCombo.getValue();
        Course course = instructorCourseCombo.getValue();
        if (instructor == null || course == null) {
            showError("Please select both an instructor and a course.");
            return;
        }
        instructorService.assignCourseToInstructor(instructor.getInstructorId(), course);
        showSuccess("Course " + course.getCourseName() + " assigned to instructor " + instructor.getName());
    }

    // --- Department Tab Actions ---
    @FXML
    public void onAddDepartment() {
        String name = departmentNameField.getText().trim();
        if (name.isEmpty()) {
            showError("Department name is required.");
            return;
        }
        departmentService.addDepartment(name);
        departmentNameField.clear();
        refreshDepartments();
        refreshSelectors();
    }

    @FXML
    public void onAddCourseToDepartment() {
        Department dept = departmentAssignCombo.getValue();
        Course course = departmentCourseCombo.getValue();
        if (dept == null || course == null) {
            showError("Please select both a department and a course.");
            return;
        }
        departmentService.addCourseToDepartment(dept.getDepartmentId(), course);
        showSuccess("Course " + course.getCourseName() + " added to department " + dept.getDepartmentName());
    }

    // --- Schedule Tab Actions ---
    @FXML
    public void onAddSchedule() {
        String day = scheduleDayField.getText().trim();
        String time = scheduleTimeField.getText().trim();
        if (day.isEmpty() || time.isEmpty()) {
            showError("Both day and time are required.");
            return;
        }
        scheduleService.addSchedule(day, time);
        scheduleDayField.clear();
        scheduleTimeField.clear();
        refreshSchedules();
    }

    // --- Fees Tab Actions ---
    @FXML
    public void onAssignFees() {
        Student student = feesStudentCombo.getValue();
        String amountText = feesAmountField.getText().trim();
        String deadline = feesDeadlineField.getText().trim();

        if (student == null || amountText.isEmpty() || deadline.isEmpty()) {
            showError("Please provide student, amount, and deadline.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            feesService.assignFees(student.getStudentId(), amount, deadline);
            feesAmountField.clear();
            feesDeadlineField.clear();
            refreshFees();
        } catch (NumberFormatException e) {
            showError("Amount must be a valid number.");
        }
    }

    @FXML
    public void onPaySelectedFees() {
        FeeRow selected = feesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a fee record to pay.");
            return;
        }
        feesService.payFees(selected.getFeeId());
        refreshFees();
    }

    // --- Refresh Data Methods ---
    @FXML
    public void onRefreshData() {
        refreshAll();
    }

    private void refreshAll() {
        refreshStudents();
        refreshCourses();
        refreshSelectors();
        refreshEnrollments();
        refreshInstructors();
        refreshDepartments();
        refreshSchedules();
        refreshFees();
    }

    private void refreshStudents() {
        studentsTable.setItems(FXCollections.observableArrayList(studentService.getStudents()));
    }

    private void refreshCourses() {
        coursesTable.setItems(FXCollections.observableArrayList(courseService.getCourses()));
    }

    private void refreshSelectors() {
        ObservableList<Student> studentList = FXCollections.observableArrayList(studentService.getStudents());
        enrollmentStudentCombo.setItems(studentList);
        feesStudentCombo.setItems(studentList);

        ObservableList<Course> courseList = FXCollections.observableArrayList(courseService.getCourses());
        enrollmentCourseCombo.setItems(courseList);
        instructorCourseCombo.setItems(courseList);
        departmentCourseCombo.setItems(courseList);

        instructorAssignCombo.setItems(FXCollections.observableArrayList(instructorService.getInstructors()));
        departmentAssignCombo.setItems(FXCollections.observableArrayList(departmentService.getDepartments()));
    }

    private void refreshEnrollments() {
        ObservableList<EnrollmentRow> rows = FXCollections.observableArrayList();
        User currentUser = AuthService.getCurrentUser();
        boolean isStudent = currentUser != null && "STUDENT".equals(currentUser.getRole());

        for (Enrollment enrollment : enrollmentService.getEnrollments()) {
            if (isStudent && enrollment.getStudentId() != currentUser.getStudentId()) {
                continue;
            }
            Student student = studentService.getStudentById(enrollment.getStudentId());
            Course course = courseService.getCourseById(enrollment.getCourseId());
            String studentName = student != null ? student.getName() : "Unknown";
            String courseName = course != null ? course.getCourseName() : "Unknown";
            rows.add(new EnrollmentRow(
                    enrollment.getEnrollmentId(), enrollment.getStudentId(), studentName,
                    enrollment.getCourseId(), courseName, enrollment.getEnrollmentDate()
            ));
        }
        enrollmentsTable.setItems(rows);
    }

    private void refreshInstructors() {
        instructorsTable.setItems(FXCollections.observableArrayList(instructorService.getInstructors()));
    }

    private void refreshDepartments() {
        departmentsTable.setItems(FXCollections.observableArrayList(departmentService.getDepartments()));
    }

    private void refreshSchedules() {
        schedulesTable.setItems(FXCollections.observableArrayList(scheduleService.getSchedules()));
    }

    private void refreshFees() {
        ObservableList<FeeRow> rows = FXCollections.observableArrayList();
        for (int i = 0; i < feesService.getFeesList().size(); i++) {
            Fees f = feesService.getFeesList().get(i);
            int studentId = -1;
            if (i < feesService.getStudentIds().size()) {
                studentId = feesService.getStudentIds().get(i);
            }
            rows.add(new FeeRow(f.getFeeId(), studentId, f.getAmount(), f.getDeadline(), f.getPaymentStatus()));
        }
        feesTable.setItems(rows);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Cannot complete action");
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
