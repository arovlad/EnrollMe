package controller;

import exceptions.AlreadyEnrolledException;
import exceptions.CourseIsFullException;
import exceptions.CreditsOverflowException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Course;
import model.Student;
import model.Teacher;

import java.sql.SQLException;

public class StudentController {
    private Controller controller;
    private int studentID;
    public Label errorMsg;
//    public GridPane mainPane;
    public Text name;
    public Text credits;
    public TableView<Course> table;

    public StudentController(){}

    public void prepareView() throws SQLException {
        Student s = this.controller.getStudent(studentID);
        this.name.setText(s.getFirstName() + " " + s.getLastName() + " (" + s.getStudentID() + ")");
        this.credits.setText("Number of credits: " + s.getTotalCredits());
        ((TableColumn)this.table.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<Course,String>("name"));
        ((TableColumn)this.table.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<Course, Teacher>("teacher"));
        ((TableColumn)this.table.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<Course, String>("places"));
        ((TableColumn)this.table.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<Course, Integer>("credits"));
        this.table.getItems().addAll(this.controller.listAllCourses());
    }

    public void setStudentID(long studentID) {
        this.studentID = (int)studentID;
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void enrollStudent(ActionEvent actionEvent) throws SQLException, CourseIsFullException, AlreadyEnrolledException, CreditsOverflowException {
        Course course = this.table.getSelectionModel().getSelectedItem();
        try {
            this.controller.enrollStudent(this.controller.getStudent(studentID), course);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (Exception e){
            this.errorMsg.setText(e.getMessage());
            this.errorMsg.setVisible(true);
        }
        ObservableList<Course> items = this.table.getItems();
        this.table.getItems().removeAll(items);
        this.table.getItems().addAll(this.controller.listAllCourses());
        this.credits.setText("Number of credits: " + this.controller.getStudent(studentID).getTotalCredits());
    }
}
