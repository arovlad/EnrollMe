package controller;
import exceptions.AlreadyEnrolledException;
import exceptions.CourseIsFullException;
import exceptions.CreditsOverflowException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Student;
import repository.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewController {
    public TextField firstNameField;
    public TextField lastNameField;
    public Label errorMsg;
    private Controller controller;

    public ViewController() throws IllegalAccessException, CourseIsFullException, CreditsOverflowException, AlreadyEnrolledException, InstantiationException, IOException, ClassNotFoundException, AlreadyInListException, SQLException {
        CourseJdbcRepository courseJdbcRepository = new CourseJdbcRepository();
        StudentJdbcRepository studentJdbcRepository = new StudentJdbcRepository(courseJdbcRepository);
        this.controller = new Controller(courseJdbcRepository, studentJdbcRepository);
    }

    public void verifyCreditentials(ActionEvent actionEvent) throws SQLException, IOException {
        List<Student> studentList = controller.listAllStudentsByName(firstNameField.getText(), lastNameField.getText());
        if (studentList.size() > 0) {
            this.studentView(studentList.get(0).getStudentID());
            return;
        }
        int TeacherID = controller.getTeacher(firstNameField.getText(), lastNameField.getText());
        if (TeacherID != 0){
            this.teacherView(TeacherID);
            return;
        }
        else errorMsg.setVisible(true);
    }

    private void teacherView(int teacherID) throws IOException, SQLException {
        Stage studentStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/teacherView.fxml"));
        Parent root = loader.load();
        TeacherController sc = loader.getController();
        sc.setTeacherID(teacherID);
        sc.setController(this.controller);
        sc.prepareView();
        studentStage.setTitle("Java University");
        studentStage.setScene(new Scene(root, 300, 275));
        studentStage.setMaximized(true);
        studentStage.show();

    }

    private void studentView(long studentID) throws IOException, SQLException {
        Stage studentStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/studentView.fxml"));
        Parent root = loader.load();
        StudentController sc = loader.getController();
        sc.setStudentID(studentID);
        sc.setController(this.controller);
        sc.prepareView();
        studentStage.setTitle("Java University");
        studentStage.setScene(new Scene(root, 300, 275));
        studentStage.setMaximized(true);
        studentStage.show();
    }
}
