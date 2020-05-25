package repository;

import model.Course;
import model.Student;
import model.Teacher;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository implements Repository<Course> {
    private List<Course> courseList = new ArrayList<>();

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
    public Course add(Course course) throws AlreadyInListException {
        if (courseList.contains(course)) {
            throw new AlreadyInListException();
        }
        courseList.add(course);
        return course;
    }

    /**
     * loscht einen Vorlesung in der Datenbank
     *
     * @param id ID der Vorlesung
     * @return der geloschte Vorlesung
     */
    public Course remove(Long id) {
        Course course = this.courseList.get(Math.toIntExact(id));
        while(course.getStudentsEnrolled().iterator().hasNext()){
            Student student = course.getStudentsEnrolled().iterator().next();
            student.setTotalCredits(student.getTotalCredits() - course.getCredits());
        }
        this.courseList.remove(id);
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
    public Course update(Course entity) {
        int pos = 0;
        for (Course course : this.courseList) {
            if (entity.getName().equals(course.getName()) && entity.getTeacher() == course.getTeacher()) {
                this.courseList.set(pos++, entity);
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
}
