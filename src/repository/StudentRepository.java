package repository;

import model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements Repository<Student> {
    private List<Student> studentList = new ArrayList<>();

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
    public Student add(Student student) throws AlreadyInListException {
        if (studentList.contains(student)) {
            throw new AlreadyInListException();
        }
        studentList.add(student);
        return student;
    }

    /**
     * loscht einen Student
     *
     * @param id id der Student
     * @return der geloschte Student
     */
    public Student remove(Long id) {
        Student student;
        while (this.studentList.iterator().hasNext()){
            student = this.studentList.iterator().next();
            if (student.getStudentID() == id){
                this.studentList.remove(student);
                return student;
            }
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
     * keine Ahnung
     *
     * @param entity entity must not be null
     * @return
     */
    public Student update(Student entity) {
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
}
