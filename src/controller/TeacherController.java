package controller;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import model.Course;
import model.Student;
import model.Teacher;

import java.sql.SQLException;

public class TeacherController {
    public Text name;
    public TableView<Course> courses;
    public TableView<Student> enrolledStudents;
    private int teacherID;
    private Controller controller;

    public TeacherController(){};

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void prepareView() throws SQLException {
        Teacher t = this.controller.getTeacherById(teacherID);
        this.name.setText(t.getName());
        ((TableColumn)this.courses.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Course,String>("name"));
        ((TableColumn)this.courses.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Course, String>("places"));
        ((TableColumn)this.courses.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<Course, Integer>("credits"));
        this.courses.getItems().addAll(t.getCourses());
        ((TableColumn)this.enrolledStudents.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        ((TableColumn)this.enrolledStudents.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Student, Integer>("studentID"));
        ((TableColumn)this.enrolledStudents.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<Student, Integer>("totalCredits"));
    }


    public void requestStudents(MouseEvent mouseEvent) {
        Course course = this.courses.getSelectionModel().getSelectedItem();
        ObservableList<Student> items = this.enrolledStudents.getItems();
        this.enrolledStudents.getItems().removeAll(items);
        this.enrolledStudents.getItems().addAll(course.getStudentsEnrolled());
    }
}
