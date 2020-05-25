package model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {
    private List<Course> courses;

    /**
     * Stellt die Klasse ein.
     *
     * @param firstName Vorname der Person.
     * @param lastName  Nachname der Person.
     */
    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
        this.courses = new ArrayList<Course>();
    }

    /**
     * gibt alle Vorlesungen der Lehrer zuruck
     *
     * @return Vorlesungen, die der Lehrer unterrichten
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * stellt die Vorlesungen der Lehrer ein
     *
     * @param courses die neue Vorlesungen der Lehrer
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * fugt einen Vorlesung in der Liste der unterrichteten Vorlesungen
     *
     * @param course der neue Vorlesung
     */
    public void addCourse(Course course) {
        course.setTeacher(this);
        this.courses.add(course);
    }

    /**
     * gibt die Representation der Lehrer als String an
     *
     * @return die Representation der Lehrer als String
     */
    public String toString() {
        return this.getName();
    }

    /**
     * vergleicht zwei Lehrer
     *
     * @param obj der andere Lehrer
     * @return true - ob die zwei Lehere gleich sind, false - anderseits
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Teacher))
            return false;
        if (!this.getLastName().equals(((Teacher) obj).getLastName()))
            return false;
        return this.getFirstName().equals(((Teacher) obj).getFirstName());
    }
}
