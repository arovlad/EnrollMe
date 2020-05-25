package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private Person teacher;
    private int maxEnrollment;
    private List<Student> studentsEnrolled;
    private int credits;

    /**
     * der Konstruktor der Klasse
     *
     * @param name Name der Vorlesung
     * @param teacher Lehrer
     * @param maxEnrollment freie Platze
     * @param credits Kreditwert der Vorlesung
     */
    public Course(String name, Person teacher, int maxEnrollment, int credits) {
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
        this.studentsEnrolled  = new ArrayList<Student>();
    }

    /**
     * gibt der Name der Vorlesung zuruck
     *
     * @return Name der Vorlesung
     */
    public String getName() {
        return name;
    }

    /**
     * gibt der Lehrer der Vorlesung zuruck
     *
     * @return Lehrer der Vorlesung
     */
    public Person getTeacher() {
        return teacher;
    }

    /**
     * gibt die Platze der Vorlesung
     *
     * @return Platze der Vorlesung
     */
    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    /**
     * gibt die Studenten die an der Vorlesung angemeldet sind zuruck
     *
     * @return angemeldeten Studenten
     */
    public List<Student> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    /**
     * gibt den Kreditwert der Vorlesung zuruck
     *
     * @return Kreditwert der Vorlesung
     */
    public int getCredits() {
        return credits;
    }

    /**
     * stellt der Name der Vorlesung ein
     *
     * @param name neuer Name der Vorlesung
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * stellt der Lehrer ein
     *
     * @param teacher neuen Lehrer
     */
    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    /**
     * stellt der Anzahl der freien Platze ein
     *
     * @param maxEnrollment neuen Anzahl der Platze
     */
    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    /**
     * stellt die angemeldeten Studenten ein
     *
     * @param studentsEnrolled die neuen angemeldeten Studenten
     */
    public void setStudentsEnrolled(List<Student> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    /**
     * meldet ein Student zu einen Vorlesung ein
     *
     * @param student angemeldeten Student
     */
    public void enrollStudent (Student student) {
        this.studentsEnrolled.add(student);
    }

    /**
     * stellt der Kreditwert der Vorlesung ein
     *
     * @param credits der neuen Kreditwert
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * gibt ob der Vorlesung voll ist zuruck
     *
     * @return true - falls der Vorlesung voll ist, false - anderseits
     */
    public boolean isFull() {
        return this.studentsEnrolled.size() >= this.maxEnrollment;
    }

    /**
     * stellt die Reprasentierung der Object als String ein
     *
     * @return die Represantierung der Object als String
     */
    public String toString() {
        return this.getName() + " | Dozent: " + this.getTeacher() + " | Kredite: " + this.getCredits() + " | Platze: " + this.getStudentsEnrolled().size() + "/" + this.getMaxEnrollment();
    }

    /**
     * stellt die Vergleichung zwitchen zwei Vorlesungen ein
     *
     * @param obj einen anderen Vorlesung
     * @return true - ob die Vorlesungen gleich sind, false - anderseits
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Course))
            return false;
        if (this.getTeacher() != ((Course) obj).getTeacher())
            return false;
        return this.getName().equals(((Course) obj).getName());
    }

    public String getPlaces(){
        return this.getStudentsEnrolled().size() + "/" + this.getMaxEnrollment();
    }
}
