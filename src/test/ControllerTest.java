package test;

import model.Course;
import model.Person;
import model.Student;
import model.Teacher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repository.AlreadyInListException;
import repository.CourseFileRepository;
import repository.CourseRepository;
import repository.StudentRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ControllerTest {
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;

    @Before
    public void CourseRepositoryTest() throws AlreadyInListException {
        this.courseRepository = new CourseRepository();
        this.courseRepository.add(new Course("MAP",new Teacher("Catalin","Rusu"), 30,15));
        this.courseRepository.add(new Course("BD",new Teacher("Diana","Troanca"), 41,11));
        this.courseRepository.add(new Course("Statistica",new Teacher("Hannelore","Brekner"), 16,7));

        this.studentRepository = new StudentRepository();
        this.studentRepository.add(new Student("John", "Doe", 220));
        this.studentRepository.add(new Student("Jane", "Doe", 221));
        this.studentRepository.add(new Student("James", "Hamilton", 222));
        this.studentRepository.add(new Student("Jane", "Hamilton", 219));
        this.studentRepository.getAll().get(0).addEnrolledCourse(this.courseRepository.getAll().get(0));
        this.studentRepository.getAll().get(0).addEnrolledCourse(this.courseRepository.getAll().get(1));
        this.studentRepository.getAll().get(3).addEnrolledCourse(this.courseRepository.getAll().get(0));
        this.studentRepository.getAll().get(3).addEnrolledCourse(this.courseRepository.getAll().get(1));
        this.studentRepository.getAll().get(3).addEnrolledCourse(this.courseRepository.getAll().get(2));
        this.studentRepository.getAll().get(2).addEnrolledCourse(this.courseRepository.getAll().get(1));
        this.studentRepository.getAll().get(1).addEnrolledCourse(this.courseRepository.getAll().get(2));
    }

    @Test
    public void addStudent() throws AlreadyInListException {
        Student student = new Student("Buzz", "Hilton", 441);
        this.studentRepository.add(student);
        assertEquals(this.studentRepository.getAll().size(), 5);
        assertEquals(this.studentRepository.getAll().get(4), student);
    }

    @Test
    public void addCourse() throws AlreadyInListException {
        Course course = new Course ("Retele de calculatoare", new Teacher("Hartmut","Konig"), 19,21);
        this.courseRepository.add(course);
        assertEquals(this.courseRepository.getAll().size(), 4);
        assertEquals(this.courseRepository.getAll().get(3), course);
    }

    @Test
    public void removeCourse() {
        Course course = this.courseRepository.getAll().get(1);
        Course course2 = this.courseRepository.getAll().get(2);
        assertEquals(this.courseRepository.getAll().remove(course), true);
        assertEquals(this.courseRepository.getAll().size(), 2);
        assertEquals(this.courseRepository.getAll().get(1),course2);
    }

    @Test
    public void enrollStudent() {
        Course course = this.courseRepository.getAll().get(1);
        this.studentRepository.getAll().get(1).addEnrolledCourse(course);
        assertEquals(this.studentRepository.getAll().get(1).getEnrolledCourses().get(1), course);
    }

    @Test
    public void listCoursesWithFreePlaces() {
        assertEquals(this.courseRepository.getCoursesWithFreePlaces(), this.courseRepository.getAll());
    }

    @Test
    public void listEnrolledStudents() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(this.studentRepository.getAll().get(0));
        studentList.add(this.studentRepository.getAll().get(3));
        studentList.add(this.studentRepository.getAll().get(2));
        assertEquals(this.courseRepository.getAll().get(1).getStudentsEnrolled(),studentList);
    }

/*    @Test
    public void listAllCourses() {
        List<Course> courseList
    }*/

    @Test
    public void listAllCoursesByName() {
        List<Course> courseList = new ArrayList<>();
        courseList.add(this.courseRepository.getAll().get(1));
        assertEquals(this.courseRepository.getByName("BD"), courseList);
    }

    @Test
    public void getValidTeacher() {
        assertEquals(this.courseRepository.getValidTeacher("Diana Troanca"), new Teacher("Diana", "Troanca"));
    }

    @Test
    public void listAllStudentsByName() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(this.studentRepository.getAll().get(1));
        assertEquals(this.studentRepository.getAllByName("Jane", "Doe"), studentList);
    }

    @Test
    public void getAllStudentsByFirstName() {
        List<Student> studentList2 = new ArrayList<>();
        studentList2.add(this.studentRepository.getAll().get(2));
        studentList2.add(this.studentRepository.getAll().get(1));
        studentList2.add(this.studentRepository.getAll().get(3));
        studentList2.add(this.studentRepository.getAll().get(0));
        List<Student> studentList = this.studentRepository.getAll();
        studentList.sort(Comparator.comparing(Person::getFirstName));
        assertEquals(studentList, studentList2);
    }

    @Test
    public void getAllStudentsByLastName() {
        List<Student> studentList2 = this.studentRepository.getAll();
        List<Student> studentList = this.studentRepository.getAll();
        studentList.sort(Comparator.comparing(Person::getLastName));
        assertEquals(studentList, studentList2);
    }

    @Test
    public void getAllStudentsByID() {
        List<Student> studentList2 = new ArrayList<>();
        studentList2.add(this.studentRepository.getAll().get(3));
        studentList2.add(this.studentRepository.getAll().get(0));
        studentList2.add(this.studentRepository.getAll().get(1));
        studentList2.add(this.studentRepository.getAll().get(2));
        List<Student> studentList = this.studentRepository.getAll();
        studentList.sort(Comparator.comparing(Student::getStudentID));
        assertEquals(studentList, studentList2);
    }

    @Test
    public void getAllCoursesByTeacher() {
        List<Course> courseList1 = this.courseRepository.getAll();
        List<Course> courseList2 = this.courseRepository.getAll();
        courseList2.sort(Comparator.comparing(object -> object.getTeacher().getName()));
        assertEquals(courseList1, courseList2);
    }

    @Test
    public void getAllCoursesByCredits() {
        List<Course> courseList1 = new ArrayList<>();
        courseList1.add(this.courseRepository.getAll().get(2));
        courseList1.add(this.courseRepository.getAll().get(1));
        courseList1.add(this.courseRepository.getAll().get(0));
        List<Course> courseList2 = this.courseRepository.getAll();
        courseList2.sort(Comparator.comparing(Course::getCredits));
        assertEquals(courseList1, courseList2);
    }

    @Test
    public void getAllCoursesByPlaces() {
        List<Course> courseList1 = new ArrayList<>();
        courseList1.add(this.courseRepository.getAll().get(2));
        courseList1.add(this.courseRepository.getAll().get(0));
        courseList1.add(this.courseRepository.getAll().get(1));
        List<Course> courseList2 = this.courseRepository.getAll();
        courseList2.sort(Comparator.comparing(Course::getMaxEnrollment));
        assertEquals(courseList1, courseList2);
    }

    @Test
    public void getAllStudentsWithEnoughCredits() {
        List<Student> studentList = new ArrayList<>();
        for (Student student : this.studentRepository.getAll()){
            if (student.getTotalCredits() >= 30) {
                studentList.add(student);
            }
        }
        List<Student> studentList2 = new ArrayList<>();
        studentList2.add(this.studentRepository.getAll().get(3));
        assertEquals(studentList,studentList2);
    }

    @Test
    public void getAllCoursesWithCredits() {
        List<Course> courseList = new ArrayList<>();
        for (Course course : this.courseRepository.getAll()){
            if (course.getCredits() > 4) {
                courseList.add(course);
            }
        }
        assertEquals(courseList,this.courseRepository.getAll());
    }

    @Test
    public void getAllStudentsWithLowerCredits() {
        List<Student> studentList = new ArrayList<>();
        for (Student student : this.studentRepository.getAll()){
            if (student.getTotalCredits() < 30) {
                studentList.add(student);
            }
        }
        List<Student> studentList1 = new ArrayList<>();
        studentList1.add(this.studentRepository.getAll().get(0));
        studentList1.add(this.studentRepository.getAll().get(1));
        studentList1.add(this.studentRepository.getAll().get(2));
        assertEquals(studentList,studentList1);
    }
}
