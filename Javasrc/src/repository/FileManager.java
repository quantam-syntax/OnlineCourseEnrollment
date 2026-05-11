package repository;

import model.Course;
import model.Department;
import model.Enrollment;
import model.Fees;
import model.Instructor;
import model.Schedule;
import model.Student;
import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private static final String STUDENTS_FILE = "students.csv";
    private static final String COURSES_FILE = "courses.csv";
    private static final String ENROLLMENTS_FILE = "enrollments.csv";
    private static final String INSTRUCTORS_FILE = "instructors.csv";
    private static final String DEPARTMENTS_FILE = "departments.csv";
    private static final String SCHEDULES_FILE = "schedules.csv";
    private static final String FEES_FILE = "fees.csv";
    private static final String USERS_FILE = "users.csv";

    // ─── Student ────────────────────────────────────────────────────────────────

    public void saveStudent(Student student) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENTS_FILE, true))) {
            bw.write(student.getStudentId() + "," + student.getName() + "," + student.getDepartment());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadStudents() {
        ArrayList<String[]> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    // ─── Course ─────────────────────────────────────────────────────────────────

    public void saveCourse(Course course) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COURSES_FILE, true))) {
            bw.write(course.getCourseId() + "," + course.getCourseName() + "," + course.getCredits());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving course: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadCourses() {
        ArrayList<String[]> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                courses.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
        return courses;
    }

    // ─── Enrollment ─────────────────────────────────────────────────────────────

    public void saveEnrollment(Enrollment enrollment) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ENROLLMENTS_FILE, true))) {
            bw.write(enrollment.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving enrollment: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadEnrollments() {
        ArrayList<String[]> enrollments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ENROLLMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                enrollments.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading enrollments: " + e.getMessage());
        }
        return enrollments;
    }

    public void deleteEnrollment(int enrollmentId) {
        ArrayList<String> remaining = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ENROLLMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) != enrollmentId) {
                    remaining.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrollments: " + e.getMessage());
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ENROLLMENTS_FILE, false))) {
            for (String line : remaining) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating enrollments: " + e.getMessage());
        }
    }

    // ─── Instructor ─────────────────────────────────────────────────────────────
    // Format: instructorId,name

    public void saveInstructor(Instructor instructor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(INSTRUCTORS_FILE, true))) {
            bw.write(instructor.getInstructorId() + "," + instructor.getName());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving instructor: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadInstructors() {
        ArrayList<String[]> instructors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INSTRUCTORS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                instructors.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading instructors: " + e.getMessage());
        }
        return instructors;
    }

    // ─── Department ─────────────────────────────────────────────────────────────
    // Format: departmentId,departmentName

    public void saveDepartment(Department department) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DEPARTMENTS_FILE, true))) {
            bw.write(department.getDepartmentId() + "," + department.getDepartmentName());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving department: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadDepartments() {
        ArrayList<String[]> departments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DEPARTMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                departments.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading departments: " + e.getMessage());
        }
        return departments;
    }

    // ─── Schedule ────────────────────────────────────────────────────────────────
    // Format: scheduleId,day,time

    public void saveSchedule(Schedule schedule) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCHEDULES_FILE, true))) {
            bw.write(schedule.getScheduleId() + "," + schedule.getDay() + "," + schedule.getTime());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving schedule: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadSchedules() {
        ArrayList<String[]> schedules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SCHEDULES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                schedules.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading schedules: " + e.getMessage());
        }
        return schedules;
    }

    // ─── Fees ────────────────────────────────────────────────────────────────────
    // Format: feeId,studentId,amount,deadline,paymentStatus

    public void saveFees(int studentId, Fees fees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEES_FILE, true))) {
            bw.write(fees.getFeeId() + "," + studentId + "," + fees.getAmount() + "," + fees.getDeadline() + "," + fees.getPaymentStatus());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving fees: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadFees() {
        ArrayList<String[]> feesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FEES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                feesList.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading fees: " + e.getMessage());
        }
        return feesList;
    }

    public void updateFeesStatus(int feeId) {
        ArrayList<String> updated = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FEES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0].trim()) == feeId) {
                    parts[4] = "Paid";
                    updated.add(String.join(",", parts));
                } else {
                    updated.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading fees: " + e.getMessage());
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FEES_FILE, false))) {
            for (String line : updated) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating fees: " + e.getMessage());
        }
    }

    // ─── User ────────────────────────────────────────────────────────────────────
    // Format: username,password,role,studentId

    public void saveUser(model.User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            bw.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole() + "," + user.getStudentId());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadUsers() {
        ArrayList<String[]> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return users;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                users.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
}