package repository;

import model.Course;
import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentFileRepository {
    private List<Student> studentList;

    public StudentFileRepository(CourseFileRepository courseLink) throws AlreadyInListException, IOException {
        this.studentList = new ArrayList<>();
        File databasePath = new File("D:\\Facultate\\MAP\\Lab3\\out\\production\\Lab3\\repository\\students.txt");
        Scanner scanner = new Scanner(databasePath);
        while (scanner.hasNextLine()){
            String[] studentInfo = scanner.nextLine().split(", ");
            Student student = new Student(studentInfo[0], studentInfo[1], Long.parseLong(studentInfo[2]));
            for (byte i = 3; i < studentInfo.length; i++){
                student.addEnrolledCourse(courseLink.getAll().get(Integer.parseInt(studentInfo[i])));
            }
            this.add(student);
        }
    }

    /**
     * gibt alle Studenten in der Datenbank zuruck
     *
     * @return alle Studenten
     */
    public List<Student> getAll() {
        return studentList;
    }

    /**
     * fugt einen Student in der Datenbank ein
     *
     * @param student der Student der eingefugt wird
     * @return der Student der eingefugt war, null falls er schon existiert
     * @throws AlreadyInListException der Student existiert schon in der Datenbank
     */
    public Student add(Student student) throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException {
        if (studentList.contains(student)) {
            throw new AlreadyInListException();
        }
        studentList.add(student);
        this.writeData();
        return student;
    }

    /**
     * loscht einen Student
     *
     * @param id id der Student
     * @return der geloschte Student
     */
    public Student remove(Long id) throws FileNotFoundException, UnsupportedEncodingException {
        for (Student student : this.studentList)
            if (student.getStudentID() == id){
                this.studentList.remove(student);
                this.writeData();
                return student;
            }
        return null;
    }

    /**
     * sucht einen Student in der Datenbank
     *
     * @param id ID der Student
     * @return der gefundene Student
     */
    public Student find(Long id) {
        Student student;
        while (this.studentList.iterator().hasNext()){
            student = this.studentList.iterator().next();
            if (student.getStudentID() == id){
                return student;
            }
        }
        return null;
    }

    /**
     * aktualisiert einen Student
     *
     * @param entity entity must not be null
     * @return der Student
     */
    public Student update(Student entity) throws FileNotFoundException, UnsupportedEncodingException {
        int pos = 0;
        for (Student student : this.studentList) {
            if (entity.getStudentID() == student.getStudentID()) {
                this.studentList.set(pos++, entity);
                this.writeData();
                return student;
            }
        }
        return null;
    }

    /**
     * gibt alle Studenten nach einen bestimmten Namen zuruck
     *
     * @param firstName Vorname
     * @param lastName Nachname
     * @return gematchten Studenten
     */
    public List<Student> getAllByName(String firstName, String lastName) {
        List<Student> studentList = new ArrayList<>();
        for (Student student : this.studentList){
            if (student.getLastName().equals(lastName))
                if (student.getFirstName().equals(firstName))
                    studentList.add(student);
        }
        return studentList;
    }

    /** aktualisiert der textbasierende Datenbank
     *
     */
    public void writeData() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("D:\\Facultate\\MAP\\Lab3\\out\\production\\Lab3\\repository\\students.txt", "UTF-8");
        for (Student student : this.getAll()){
            writer.print(student.getFirstName() + ", " + student.getLastName() + ", " + student.getStudentID());
            for (int i = 0 ; i < student.getEnrolledCourses().size() ; i++){
                writer.print(", " + i);
            }
            writer.print("\n");
        }
        writer.close();
    }
}
