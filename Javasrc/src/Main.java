import service.CourseService;
import service.DepartmentService;
import service.EnrollmentService;
import service.FeesService;
import service.InstructorService;
import service.ScheduleService;
import service.StudentService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = new EnrollmentService(courseService);
        InstructorService instructorService = new InstructorService();
        DepartmentService departmentService = new DepartmentService();
        ScheduleService scheduleService = new ScheduleService();
        FeesService feesService = new FeesService();

        while (true) {
            System.out.println("\n===== Online Course Enrollment System =====");
            System.out.println("--- Student ---");
            System.out.println("1.  Register Student");
            System.out.println("2.  View All Students");
            System.out.println("--- Course ---");
            System.out.println("3.  Add Course");
            System.out.println("4.  View Available Courses");
            System.out.println("--- Enrollment ---");
            System.out.println("5.  Enroll in Course");
            System.out.println("6.  Drop Course");
            System.out.println("7.  View My Enrollments");
            System.out.println("--- Instructor ---");
            System.out.println("8.  Add Instructor");
            System.out.println("9.  View All Instructors");
            System.out.println("10. Assign Course to Instructor");
            System.out.println("--- Department ---");
            System.out.println("11. Add Department");
            System.out.println("12. View All Departments");
            System.out.println("13. Add Course to Department");
            System.out.println("--- Schedule ---");
            System.out.println("14. Add Schedule");
            System.out.println("15. View All Schedules");
            System.out.println("--- Fees ---");
            System.out.println("16. Assign Fees to Student");
            System.out.println("17. Pay Fees");
            System.out.println("18. View Fees by Student");
            System.out.println("---");
            System.out.println("19. Exit");
            System.out.print("Choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter department: ");
                        String dept = sc.nextLine();
                        studentService.registerStudent(name, dept);
                        break;

                    case 2:
                        studentService.displayAllStudents();
                        break;

                    case 3:
                        System.out.print("Enter course name: ");
                        String cName = sc.nextLine();
                        System.out.print("Enter credits: ");
                        int credits = sc.nextInt();
                        courseService.addCourse(cName, credits);
                        break;

                    case 4:
                        courseService.viewCourses();
                        break;

                    case 5:
                        System.out.print("Enter your student ID: ");
                        int sId = sc.nextInt();
                        System.out.print("Enter course ID: ");
                        int cId = sc.nextInt();
                        enrollmentService.selectCourse(sId, cId);
                        break;

                    case 6:
                        System.out.print("Enter enrollment ID to drop: ");
                        int eId = sc.nextInt();
                        enrollmentService.dropCourse(eId);
                        break;

                    case 7:
                        System.out.print("Enter your student ID: ");
                        int sid = sc.nextInt();
                        enrollmentService.viewEnrollments(sid);
                        break;

                    case 8:
                        System.out.print("Enter instructor name: ");
                        String iName = sc.nextLine();
                        instructorService.addInstructor(iName);
                        break;

                    case 9:
                        instructorService.displayAllInstructors();
                        break;

                    case 10:
                        System.out.print("Enter instructor ID: ");
                        int instId = sc.nextInt();
                        System.out.print("Enter course ID: ");
                        int instCourseId = sc.nextInt();
                        model.Course course = courseService.getCourseById(instCourseId);
                        if (course != null) {
                            instructorService.assignCourseToInstructor(instId, course);
                        } else {
                            System.out.println("Course not found");
                        }
                        break;

                    case 11:
                        System.out.print("Enter department name: ");
                        String dName = sc.nextLine();
                        departmentService.addDepartment(dName);
                        break;

                    case 12:
                        departmentService.displayAllDepartments();
                        break;

                    case 13:
                        System.out.print("Enter department ID: ");
                        int depId = sc.nextInt();
                        System.out.print("Enter course ID: ");
                        int depCourseId = sc.nextInt();
                        model.Course depCourse = courseService.getCourseById(depCourseId);
                        if (depCourse != null) {
                            departmentService.addCourseToDepartment(depId, depCourse);
                        } else {
                            System.out.println("Course not found");
                        }
                        break;

                    case 14:
                        System.out.print("Enter day (e.g. Monday): ");
                        String day = sc.nextLine();
                        System.out.print("Enter time (e.g. 10:00 AM): ");
                        String time = sc.nextLine();
                        scheduleService.addSchedule(day, time);
                        break;

                    case 15:
                        scheduleService.viewAllSchedules();
                        break;

                    case 16:
                        System.out.print("Enter student ID: ");
                        int fStudentId = sc.nextInt();
                        System.out.print("Enter fee amount: ");
                        double amount = sc.nextDouble();
                        sc.nextLine();
                        System.out.print("Enter deadline (e.g. 2025-12-31): ");
                        String deadline = sc.nextLine();
                        feesService.assignFees(fStudentId, amount, deadline);
                        break;

                    case 17:
                        System.out.print("Enter fee ID to pay: ");
                        int feeId = sc.nextInt();
                        feesService.payFees(feeId);
                        break;

                    case 18:
                        System.out.print("Enter student ID: ");
                        int fViewId = sc.nextInt();
                        feesService.viewFeesByStudent(fViewId);
                        break;

                    case 19:
                        System.out.println("Goodbye!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                sc.nextLine();
            }
        }
    }
}