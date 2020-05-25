package repository;

import model.Course;
import model.Student;
import model.Teacher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CourseFileRepository implements Repository<Course> {
    private List<Course> courseList;

    public CourseFileRepository() throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException {
        this.courseList = new ArrayList<>();
        File databasePath = new File("D:\\Facultate\\MAP\\Lab3\\out\\production\\Lab3\\repository\\courses.txt");
        Scanner scanner = new Scanner(databasePath);

        while (scanner.hasNextLine()){
            String[] courseInfo = scanner.nextLine().split(", ");

            this.add(new Course(courseInfo[0],
                    this.getValidTeacher(courseInfo[1] +  " " + courseInfo[2]),
                    Integer.parseInt(courseInfo[3]),
                    Integer.parseInt(courseInfo[4])));
        }
    }

    /**
     * gibt alle Vorlesungen in der Datenbank zuruck
     *
     * @return alle Vorlesungen
     */
    public List<Course> getAll() {
        return courseList;
    }

    /**
     * gibt alle Vorlesungen mit freien Platze
     *
     * @return Vorlesungen mit freien Platze
     */
    public List<Course> getCoursesWithFreePlaces() {
        List<Course> coursesWithFreePlaces = new ArrayList<Course>();
        for (Course course : this.courseList) {
            if (!course.isFull()){
                coursesWithFreePlaces.add(course);
            }
        }
        return coursesWithFreePlaces;
    }

    /**
     * gibt alle Studenten, die zu einen bestimmten Vorlesung angemeldet sind
     *
     * @param course Vorlesung
     * @return angemeldete Studenten
     */
    public List<Student> getEnrolledStudents (Course course) {
        return course.getStudentsEnrolled();
    }

    /**
     * fugt einen neuen Vorlesung in der Datenbank ein
     *
     * @param course Vorlesung
     * @return der eingefugte Vorlesung
     * @throws AlreadyInListException die Vorlesung existiert schon in der Datenbank
     */
    public Course add(Course course) throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException {
        if (courseList.contains(course)) {
            throw new AlreadyInListException();
        }
        courseList.add(course);
        this.writeData();
        return course;
    }

    /**
     * loscht einen Vorlesung in der Datenbank
     *
     * @param id ID der Vorlesung
     * @return der geloschte Vorlesung
     */
    public Course remove(Long id) throws FileNotFoundException, UnsupportedEncodingException {
        Course course = this.courseList.get(Math.toIntExact(id));
        for(Student student : course.getStudentsEnrolled()){
            student.setTotalCredits(student.getTotalCredits() - course.getCredits());
        }
        this.courseList.remove(id);
        this.writeData();
        return course;
    }

    /**
     * sucht einen Vorlesung in der Datenbank
     *
     * @param id der id der gesuchte Vorlesung
     * @return der gefundene Vorlesung, null - anderseits
     */
    public Course find(Long id) {
        if (this.courseList.size() <= id+1)
            return this.courseList.get(Math.toIntExact(id));
        return null;
    }

    /**
     * aktualisiert einen Vorlesung
     *
     * @param entity entity must not be null
     * @return der Vorlesung
     */
    public Course update(Course entity) throws FileNotFoundException, UnsupportedEncodingException {
        int pos = 0;
        for (Course course : this.courseList) {
            if (entity.getName().equals(course.getName()) && entity.getTeacher() == course.getTeacher()) {
                this.courseList.set(pos++, entity);
                this.writeData();
                return course;
            }
        }
        return null;
    }

    /**
     * gibt Vorlesungen mit einen bestimmten Name zuruck, falls es mehrere in der Datenbank gibt
     *
     * @param name Name der Vorlesung
     * @return alle gematchten Vorlesungen
     */
    public List<Course> getByName(String name) {
        List<Course> courseMatchedList = new ArrayList<>();
        for (Course course : this.courseList){
            if (course.getName().equals(name)) {
                courseMatchedList.add(course);
            }
        }
        return courseMatchedList;
    }

    /**
     * gibt einen validen Lehrer zuruck
     *
     * @param name name der Lehrer
     * @return einen vorig existierender Leher oder einen neuen Lehrer anderseits
     */
    public Teacher getValidTeacher(String name) {
        if (!this.courseList.isEmpty()) {
            for (Course course : this.courseList) {
                String teacherName = course.getTeacher().getName();
                if (teacherName.equals(name))
                    return (Teacher) course.getTeacher();
            }
        }
        String firstName = name.substring(0,name.lastIndexOf(" "));
        String lastName =  name.substring(name.lastIndexOf(" ") + 1);
        return new Teacher(firstName, lastName);
    }

    /** aktualisiert der textbasierende Datenbank
     *
     */
    public void writeData() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("D:\\Facultate\\MAP\\Lab3\\out\\production\\Lab3\\repository\\courses.txt", "UTF-8");

        for (Course course : this.getAll()){
            writer.println(course.getName() + ", " + course.getTeacher().getFirstName() + ", " + course.getTeacher().getLastName() + ", " +
                    course.getMaxEnrollment() + ", " + course.getCredits());
        }
        writer.close();
    }
}
