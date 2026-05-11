package service;
import model.Student;
import repository.FileManager;
import java.util.ArrayList;

public class StudentService {
    private ArrayList<Student>students;
    private FileManager fileManager;
    private int studentCounter = 1;
    public StudentService(){
        this.students = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }
    public void loadFromFile(){
        ArrayList<String[]> data = fileManager.loadStudents();
        for(String[]parts : data ){
            if (parts.length != 3) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                students.add(new Student(id, id, parts[1], parts[2]));
                studentCounter = Math.max(studentCounter, id + 1);
            } catch (NumberFormatException ex) {
                System.out.println("Skipping malformed student row: " + String.join(",", parts));
            }
        }
    }
    public Student registerStudent(String name, String department){
        Student student = new Student(studentCounter , studentCounter , name , department);
        studentCounter++;
        students.add(student);
        fileManager.saveStudent(student);
        System.out.println("Student registered ID : "+ student.getStudentId() );
        return student;
    }
    public Student getStudentById(int studentId){
        for (Student s : students){
            if (s.getStudentId() == studentId){
                return s;
            }
        }
        return null;
    }
    public void displayAllStudents(){
        if (students.isEmpty()){
            System.out.println("No students registered ");
            return;
        }
        for (Student s : students ){
            s.displayInfo();
        }
    }
        public ArrayList<Student> getStudents(){
            return students;
        }


        
    }
