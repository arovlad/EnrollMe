package repository;
import java.sql.*;

//import com.microsoft.sqlserver.jdbc.SQLServerException;
import model.Course;
import model.Person;
import model.Student;
import model.Teacher;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CourseJdbcRepository implements Repository<Course> {
    final String databasePath = "jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;databaseName=JavaUniversity;integratedSecurity=true";

    /**
     * gibt alle Vorlesungen in der Datenbank zuruck
     *
     * @return alle Vorlesungen
     */
    public List<Course> getAll() throws SQLException {
        List<Course> courseList = new ArrayList<>();

        Connection connection = DriverManager.getConnection(databasePath);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select c.[Name], c.MaxEnrollment, c.Credits, t.FirstName, t.LastName from Courses c join Teachers t on c.TeacherID = t.ID");
        while (result.next()) {
            String courseName = result.getString("Name");
            short maxEnrollment = result.getShort("MaxEnrollment");
            byte credits = result.getByte("Credits");
            Teacher teacher = getValidTeacher(result.getString("FirstName") + " " + result.getString("LastName"));
            Course course = new Course(courseName, teacher, maxEnrollment, credits);
            course.setStudentsEnrolled(this.getEnrolledStudents(course));
            courseList.add(course);
        }
        statement.close();
        connection.close();
        return courseList;
    }

    /**
     * gibt alle Vorlesungen mit freien Platze
     *
     * @return Vorlesungen mit freien Platze
     */
    public List<Course> getCoursesWithFreePlaces() throws SQLException {
        List<Course> courseList = new ArrayList<>();
        Connection connection = DriverManager.getConnection(databasePath);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from Courses c join Teachers t on c.TeacherID = t.ID where (select COUNT(CourseID) from Enrolled where CourseID = c.ID) < c.MaxEnrollment;");
        while (result.next()) {
            String courseName = result.getString("Name");
            short maxEnrollment = result.getShort("MaxEnrollment");
            byte credits = result.getByte("Credits");
            Teacher teacher = getValidTeacher(result.getString("FirstName") + " " + result.getString("LastName"));
            Course course = new Course(courseName, teacher, maxEnrollment, credits);
            course.setStudentsEnrolled(this.getEnrolledStudents(course));
            courseList.add(course);
        }
        statement.close();
        connection.close();
        return courseList;
    }

    /**
     * gibt alle Studenten, die zu einen bestimmten Vorlesung angemeldet sind
     *
     * @param course Vorlesung
     * @return angemeldete Studenten
     */
    public List<Student> getEnrolledStudents(Course course) throws SQLException {
        // not ready
        List<Student> studentList = new ArrayList<>();
        Connection connection = DriverManager.getConnection(databasePath);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from Students s join Enrolled e on s.ID = e.StudentID where e.CourseID = " + this.findID(course));
        while (result.next()) {
            studentList.add(new Student(result.getString("FirstName"), result.getString("LastName"), result.getInt("ID")));
        }
        statement.close();
        connection.close();
        return studentList;
    }

    public int findID(Course course) throws SQLException {
        Connection connection = DriverManager.getConnection(databasePath);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select ID from Courses where [Name] = '" + course.getName() + "' and MaxEnrollment = " + course.getMaxEnrollment() + " and Credits = " + course.getCredits());
        while (result.next()) {
            return result.getInt("ID");
        }
        statement.close();
        connection.close();
        return 0;
    }

    /**
     * fugt einen neuen Vorlesung in der Datenbank ein
     *
     * @param course Vorlesung
     * @return der eingefugte Vorlesung
     * @throws AlreadyInListException die Vorlesung existiert schon in der Datenbank
     */
    public Course add(Course course) throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException, SQLException {
        if (this.findID(course) != 0) {
            throw new AlreadyInListException();
        }
        Connection connection = DriverManager.getConnection(databasePath);
        if (this.findTeacherID(course.getTeacher()) == 0) {
            connection.createStatement().execute("insert into Teachers(FirstName, LastName) values ('" + course.getTeacher().getFirstName() + "', '" + course.getTeacher().getLastName() + "')");
        }
        try {
            connection.createStatement().execute("insert into Courses([Name], TeacherID, MaxEnrollment, Credits) values ('" + course.getName() + "', " + this.findTeacherID(course.getTeacher()) + ", " + course.getMaxEnrollment() + ", " + course.getCredits() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return course;
    }

    public int findTeacherID(Person teacher) throws SQLException {
        Connection connection = DriverManager.getConnection(databasePath);
        ResultSet result = connection.createStatement().executeQuery("select ID from Teachers where FirstName = '" + teacher.getFirstName() + "' and LastName = '" + teacher.getLastName() + "'");
        if (result.next()) {
            return result.getInt("ID");
        }
        result.close();
        connection.close();
        return 0;
    }

    /**
     * loscht einen Vorlesung in der Datenbank
     *
     * @param id ID der Vorlesung
     * @return der geloschte Vorlesung
     */
    public Course remove(Long id) throws FileNotFoundException, UnsupportedEncodingException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Courses c join Teachers t on c.TeacherID = t.ID where c.ID = " + id);
        if (result.next()) {
            Course course = new Course(result.getString("Name"),
                    getValidTeacher(result.getString("FirstName") + " " + result.getString("LastName")),
                    result.getShort("MaxEnrollment"), result.getByte("Credits"));
            statement.execute("use JavaUniversity delete from Enrolled where CoursesID = " + id);
            // scadem numarul de credite   connection.createStatement().execute("update table Students set Credits = " + ( - course.getCredits()) + " where ");
            connection.createStatement().execute("use JavaUniversity delete from Courses where ID = " + id);
            statement.close();
            connection.close();
            return course;
        }
        statement.close();
        connection.close();
        return null;
    }

    /**
     * sucht einen Vorlesung in der Datenbank
     *
     * @param id der id der gesuchte Vorlesung
     * @return der gefundene Vorlesung, null - anderseits
     */
    public Course find(Long id) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Courses c join Teachers t on c.TeacherID = t.ID where c.ID = " + id);
        if (result.next()) {
            return new Course(result.getString("Name"),
                    getValidTeacher(result.getString("FirstName") + " " + result.getString("LastName")),
                    result.getShort("MaxEnrollment"), result.getByte("Credits"));
        }
        statement.close();
        connection.close();
        return null;
    }

    /**
     * aktualisiert einen Vorlesung
     *
     * @param entity entity must not be null
     * @return der Vorlesung
     */
    public Course update(Course entity) throws FileNotFoundException, UnsupportedEncodingException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        int id = this.findID(entity);
        if (id == 0)
            return null;
        statement.execute("update Courses set [Name] = '" + entity.getName() + "', TeacherID = " +
                this.findTeacherID(getValidTeacher(entity.getTeacher().getFirstName() + " " +
                        entity.getTeacher().getLastName())) + ", MaxEnrollment = " + entity.getMaxEnrollment() +
                ", Credits = " + entity.getCredits() + " where ID = " + this.findID(entity));
        statement.close();
        connection.close();
        return null;
    }

    /**
     * gibt Vorlesungen mit einen bestimmten Name zuruck, falls es mehrere in der Datenbank gibt
     *
     * @param name Name der Vorlesung
     * @return alle gematchten Vorlesungen
     */
    public List<Course> getByName(String name) throws SQLException {
        List<Course> courseMatchedList = new ArrayList<>();
        Connection connection = DriverManager.getConnection(databasePath);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select c.[Name], c.MaxEnrollment, c.Credits, t.FirstName, t.LastName from Courses c join Teachers t on c.TeacherID = t.ID where c.[Name] = '" + name + "'");
        while (result.next()) {
            String courseName = result.getString("Name");
            short maxEnrollment = result.getShort("MaxEnrollment");
            byte credits = result.getByte("Credits");
            Teacher teacher = getValidTeacher(result.getString("FirstName") + " " + result.getString("LastName"));
            Course course = new Course(courseName, teacher, maxEnrollment, credits);
            courseMatchedList.add(course);
        }
        statement.close();
        connection.close();
        return courseMatchedList;
    }

    /**
     * gibt einen validen Lehrer zuruck
     *
     * @param name name der Lehrer
     * @return einen vorig existierender Leher oder einen neuen Lehrer anderseits
     */
    public Teacher getValidTeacher(String name) {
        // if exists in the database with name ex
        String firstName = name.substring(0, name.lastIndexOf(" "));
        String lastName = name.substring(name.lastIndexOf(" ") + 1);
        return new Teacher(firstName, lastName);
    }

    public Teacher findTeacher(long teacherID) throws SQLException {
        Connection connection = DriverManager.getConnection(databasePath);
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("select * from Teachers where ID = " + teacherID);
        if (result.next()) {
            Teacher teacher = new Teacher(result.getString("FirstName"), result.getString("LastName"));
            result = connection.createStatement().executeQuery("select * from Courses where TeacherID = " + teacherID);
            List<Course> courseList = new ArrayList<>();
            for (Course course : this.getAll()){
                if (course.getTeacher().equals(teacher)){
                    courseList.add(course);
                }
            }
            teacher.setCourses(courseList);
            statement.close();
            connection.close();
            return teacher;
        }
        statement.close();
        connection.close();
        return null;
    }
}
