package controller;

import exceptions.AlreadyEnrolledException;
import exceptions.CourseIsFullException;
import exceptions.CreditsOverflowException;
import model.Course;
import model.Person;
import model.Student;
import model.Teacher;
import repository.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Controller {
    private CourseJdbcRepository courseRepository;
    private StudentJdbcRepository studentRepository;

    /**
     * Der Konstrukor der Kontroller.
     *
     * @param courseRepository Datenbank fur den Vorlesungen
     * @param studentRepository Datenbank fur den Studenten
     */
    public Controller(CourseJdbcRepository courseRepository, StudentJdbcRepository studentRepository) throws FileNotFoundException, AlreadyInListException, CourseIsFullException, AlreadyEnrolledException, CreditsOverflowException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Fugt einen Student in der Datenbank
     *
     * @param student Student, der hinzugefugt wird
     * @return Student, der hinzugefugt war
     * @throws AlreadyInListException der Student ist schon in der Datenbank
     */
    public Student addStudent(Student student) throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException, SQLException {
        return this.studentRepository.add(student);
    }

    /**
     * Fugt einen Vorlesung in der Datenbank
     *
     * @param course Vorlesung, die hinzugefugt wird
     * @return Vorlesung, die hinzugefugt war
     * @throws AlreadyInListException der Vorlesung ist schon in der Datenbank
     */
    public Course addCourse(Course course) throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException, SQLException {
        return this.courseRepository.add(course);
    }

    /**
     * Loscht einen Vorlesung
     *
     * @param course Vorlesung, die man loschen will
     * @return Vorlesung, die geloscht war
     */
    public Course removeCourse(Course course) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
        this.courseRepository.remove((long) this.courseRepository.findID(course));
        return course;
    }

    /**
     * Melden einen Student in einen Vorlesung ein.
     *
     * @param student der angemeldete Student
     * @param course der Vorlesung indem der Student anmeldet
     * @return true - falls der Student angemeldet ist, false - anderseits
     * @throws CreditsOverflowException der Student hat seine Kreditanzahl ubergeschritten
     * @throws CourseIsFullException der Vorlesung hat keine Platze frei
     * @throws AlreadyEnrolledException der Student wurde schon in der Vorlesung angemeldet
     */
    public boolean enrollStudent(Student student, Course course) throws CreditsOverflowException, CourseIsFullException, AlreadyEnrolledException, SQLException {
        if (course.isFull()){
            throw new CourseIsFullException();
        }
        if (student.getTotalCredits() + course.getCredits() > 30) {
            throw new CreditsOverflowException();
        }
        if (student.isEnrolled(course)){
            throw new AlreadyEnrolledException();
        }

        student.addEnrolledCourse(course);
        this.studentRepository.enrollStudent(student,course);
        return true;
    }

    /**
     * Gibt Vorlesungen, die freie Platze noch haben, zuruck
     *
     * @return Vorlesungen mit freie Platze
     */
    public List<Course> listCoursesWithFreePlaces() throws SQLException {
        return this.courseRepository.getCoursesWithFreePlaces();
    }

    /**
     * Gibt die Studenten die zu einen Vorlesung angemeldet sind zuruck
     *
     * @param course der Vorlesung die die Studenten angemeldet sind
     * @return die angemeldeten Studenten
     */
    public List<Student> listEnrolledStudents(Course course){
        return course.getStudentsEnrolled();
    }

    /**
     * Gibt alle Vorlesungen in den Datenbank zuruck
     *
     * @return Vorlesungen in der Datenbank
     */
    public List<Course> listAllCourses() throws SQLException {
        return this.courseRepository.getAll();
    }

    /**
     * Gibt alle Vorlesungen die einen bestimmten Name haben, falls sie mehrere sind
     *
     * @param name der Name der Vorlesungen
     * @return alle gematchten Vorlesungen
     */
    public List<Course> listAllCoursesByName(String name) throws SQLException {
        return this.courseRepository.getByName(name);
    }

    /**
     * Gibt einen validen Lehrer zuruck mit der gegebenen Name
     *
     * @param name Name der Lehrer
     * @return einen vorig existierender Lehrer oder einen neu erstellten Lehrer anderseits
     */
    public Teacher getValidTeacher(String name) {
        return this.courseRepository.getValidTeacher(name);
    }

    /**
     * Gibt alle Studenten die einen bestimmten Namen haben, falls es mehrere gibt
     *
     * @param firstName Vorname der Studenten
     * @param lastName Nachname der Studenten
     * @return alle gematchten Studenten
     */
    public List<Student> listAllStudentsByName(String firstName, String lastName) throws SQLException {
        return this.studentRepository.getAllByName(firstName,lastName);
    }

    /**
     * Gibt alle Studenten sortiert nach der Vorname zuruck
     * @return Studentenlist
     */
    public List<Student> getAllStudentsByFirstName() throws SQLException {
        List<Student> studentList = this.studentRepository.getAll();
        studentList.sort(Comparator.comparing(Person::getFirstName));
        return studentList;
    }

    /**
     * Gibt alle Studenten sortiert nach der Nachname zuruck
     * @return Studentenlist
     */
    public List<Student> getAllStudentsByLastName() throws SQLException {
        List<Student> studentList = this.studentRepository.getAll();
        studentList.sort(Comparator.comparing(Person::getLastName));
        return studentList;
    }

    /**
     * Gibt alle Studenten sortiert nach der ID zuruck
     * @return Studentenliste
     */
    public List<Student> getAllStudentsByID() throws SQLException {
        List<Student> studentList = this.studentRepository.getAll();
        studentList.sort(Comparator.comparing(Student::getStudentID));
        return studentList;
    }

    /**
     * Gibt alle Vorlesungen sortiert nach der Dozent zuruck
     * @return Vorlesungsliste
     */
    public List<Course> getAllCoursesByTeacher() throws SQLException {
        List<Course> courseList = this.courseRepository.getAll();
        courseList.sort(Comparator.comparing(object -> object.getTeacher().getName()));
        return courseList;
    }

    /**
     * Gibt alle Vorlesungen sortiert nach der Kredite zuruck
     * @return Vorlesungliste
     */
    public List<Course> getAllCoursesByCredits() throws SQLException {
        List<Course> courseList = this.courseRepository.getAll();
        courseList.sort(Comparator.comparing(Course::getCredits));
        return courseList;
    }

    /**
     * Gibt alle Vorlesungen sortiert nach der Anzahl der freien Platzen zuruck
     * @return Vorlesungliste
     */
    public List<Course> getAllCoursesByPlaces() throws SQLException {
        List<Course> courseList = this.courseRepository.getAll();
        courseList.sort(Comparator.comparing(Course::getMaxEnrollment));
        return courseList;
    }

    /**
     * Gibt alle Studenten sortiert mit einen genugenden Kreditanzahl zuruck
     * @return Vorlesungliste
     */
    public List<Student> getAllStudentsWithEnoughCredits() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        for (Student student : this.studentRepository.getAll()){
            if (student.getTotalCredits() >= 30) {
                studentList.add(student);
            }
        }
        return studentList;
    }

    /**
     * Gibt alle Vorlesungen sortiert nach den Kreditanzahl zuruck
     * @return Vorlesungliste
     */
    public List<Course> getAllCoursesWithCredits() throws SQLException {
        List<Course> courseList = new ArrayList<>();
        for (Course course : this.courseRepository.getAll()){
            if (course.getCredits() > 4) {
                courseList.add(course);
            }
        }
        return courseList;
    }

    /**
     * Gibt alle Studenten sortiert mit einen nicht genugenden Kreditanzahl zuruck
     * @return Vorlesungliste
     */
    public List<Student> getAllStudentsWithLowerCredits() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        for (Student student : this.studentRepository.getAll()){
            if (student.getTotalCredits() < 30) {
                studentList.add(student);
            }
        }
        return studentList;
    }

    public void saveData() throws FileNotFoundException, UnsupportedEncodingException, SQLException {
      //  this.courseRepository.writeData();
      //  this.studentRepository.writeData();
    }

    public int getTeacher(String firstName, String lastName) throws SQLException {
        return this.courseRepository.findTeacherID(new Teacher(firstName, lastName));
    }

    public Student getStudent(long id) throws SQLException {
        return this.studentRepository.find(id);
    }


    public Teacher getTeacherById(int teacherID) throws SQLException {
        return this.courseRepository.findTeacher((long)teacherID);
    }
}
