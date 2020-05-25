package repository;

import exceptions.AlreadyEnrolledException;
import model.Course;
import model.Student;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentJdbcRepository implements Repository<Student> {
    private CourseJdbcRepository courseLink;

    public StudentJdbcRepository(CourseJdbcRepository courseLink) throws AlreadyInListException, IOException, SQLException {
        this.courseLink = courseLink;
    }

    /**
     * gibt alle Studenten in der Datenbank zuruck
     *
     * @return alle Studenten
     */
    public List<Student> getAll() throws SQLException {
        /**
         * adauga intai toti studentii intr-o lista si dupa pune-le cursurile la fiecare
         */
        List<Student> studentList = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Students");
        while (result.next()) {
            Student student = new Student(result.getString("FirstName"), result.getString("LastName"), result.getLong("ID"));
            studentList.add(student);
        }
        for (Student student : studentList){
            result = connection.createStatement().executeQuery("use JavaUniversity select * from Enrolled where StudentID = " + student.getStudentID());
            List<Integer> courseIDList = new ArrayList<>();
            while (result.next()){
                courseIDList.add((int)result.getByte("CourseID"));
            }
            for (int id : courseIDList){
                student.addEnrolledCourse(this.courseLink.find((long)id));
            }
        }
        connection.close();
        return studentList;
    }

    /**
     * fugt einen Student in der Datenbank ein
     *
     * @param student der Student der eingefugt wird
     * @return der Student der eingefugt war, null falls er schon existiert
     * @throws AlreadyInListException der Student existiert schon in der Datenbank
     */
    public Student add(Student student) throws AlreadyInListException, FileNotFoundException, UnsupportedEncodingException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Students where ID = " + student.getStudentID());
        if (result.next()) {
            throw new AlreadyInListException();
        }
        statement.execute("use JavaUniversity insert into Students values('" + student.getFirstName() + "', '" +
                student.getLastName() + "', " + student.getStudentID() + ", " + student.getTotalCredits() + ")");
        statement.close();
        result.close();
        return student;
    }

    /**
     * loscht einen Student
     *
     * @param id id der Student
     * @return der geloschte Student
     */
    public Student remove(Long id) throws FileNotFoundException, UnsupportedEncodingException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Students where ID = " + id);
        if (result.next()){
            Student student = new Student(result.getString("FirstName"),result.getString("LastName"),result.getInt("ID"));
            connection.createStatement().execute("use JavaUniversity delete from Students where ID = " + id);
            statement.close();
            connection.close();
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
    public Student find(Long id) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Students where ID = " + id);
        if (result.next()){
            Student s = new Student(result.getString("FirstName"),result.getString("LastName"),result.getInt("ID"));
            s.setTotalCredits(result.getInt("Credits"));
            statement.close();
            connection.close();
            return s;
        }
        statement.close();
        connection.close();
        return null;
    }

    /**
     * aktualisiert einen Student
     *
     * @param entity entity must not be null
     * @return der Student
     */
    public Student update(Student entity) throws FileNotFoundException, UnsupportedEncodingException, SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Students where ID = " + entity.getStudentID());
        if (result.next()){
            Student student = new Student(result.getString("FirstName"),result.getString("LastName"),result.getInt("ID"));
            connection.createStatement().execute("use JavaUniversity update Students set FirstName = '" + entity.getFirstName() + "' and LastName = '" + entity.getLastName() + "' where ID = " + entity.getStudentID());
            statement.close();
            connection.close();
            return student;
        }
        statement.close();
        connection.close();
        return null;
    }

    /**
     * gibt alle Studenten nach einen bestimmten Namen zuruck
     *
     * @param firstName Vorname
     * @param lastName Nachname
     * @return gematchten Studenten
     */
    public List<Student> getAllByName(String firstName, String lastName) throws SQLException {
        List<Student> studentList = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("use JavaUniversity select * from Students where FirstName = '" + firstName + "' and LastName = '" + lastName + "'");
        if (result.next())
            studentList.add(new Student(result.getString("FirstName"), result.getString("LastName"), result.getInt("ID")));
        result.close();
        statement.close();
        connection.close();
        return studentList;
    }

    public void enrollStudent(Student student, Course course) throws SQLException, AlreadyEnrolledException {
        int courseID = this.courseLink.findID(course);
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-FQ5TC0U\\SQLEXPRESS;integratedSecurity=true");
/*        ResultSet result = connection.createStatement().executeQuery("use JavaUniversity select * from Enrolled where StudentID = " + student.getStudentID() + " and CourseID = " + courseID );
        if (result.next()){
            throw new AlreadyEnrolledException();
        }
        result = connection.createStatement().executeQuery("use JavaUniversity select COUNT(StudentID) from Enrolled where CourseID = ");*/

        connection.createStatement().execute("use JavaUniversity insert into Enrolled values (" + student.getStudentID() + ", " + courseID + ")" );
        connection.createStatement().execute("use JavaUniversity update Students set Credits = " + (student.getTotalCredits() + course.getCredits()) + " where ID = " + student.getStudentID());
        connection.close();
    }
}
