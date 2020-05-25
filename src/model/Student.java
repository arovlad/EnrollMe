package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private long studentID;
    private int totalCredits;
    private List<Course> enrolledCourses;

    public Student(String firstName, String lastName, long studentID) {
        super(firstName, lastName);
        this.studentID = studentID;
        this.enrolledCourses = new ArrayList<Course>();
    }

    public long getStudentID() {
        return studentID;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public void addToTotalCredits (int credits) {
        this.totalCredits += credits;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void addEnrolledCourse(Course course) {
        course.getStudentsEnrolled().add(this);
        this.enrolledCourses.add(course);
        this.addToTotalCredits(course.getCredits());
    }

    public boolean isEnrolled(Course course) {
        return this.enrolledCourses.contains(course);
    }

    public String toString() {
        return super.toString() + " (" + getStudentID() + ")";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Student))
            return false;
        return this.getStudentID() == ((Student)obj).getStudentID();
    }
}
