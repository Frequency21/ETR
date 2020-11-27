package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.dao.StudentDAO;
import main.dao.SubjectDAO;
import main.model.Lecture;
import main.model.Student;
import main.model.Subject;

import java.net.URL;
import java.util.ResourceBundle;

public class SubjectTabController implements Initializable {
    public TableView<Student> tvStudents;
    public TableColumn<Student, String> colStudentEtrCode;
    public TableColumn<Student, String> colStudentFirstName;
    public TableColumn<Student, String> colStudentLastName;
    public TableColumn<Student, Short> colStudentYear;
    public TableColumn<Student, String> colStudentMajor;

    public TableView<Subject> tvSubjects;
    public TableColumn<Subject, String> colSubjectCourseCode;
    public TableColumn<Subject, String> colSubjectName;
    public TableColumn<Subject, Short> colSubjectCredit;
    public TableColumn<Subject, Byte> colSubjectGrade;
    public TableColumn<Subject, Short> colSubjectYear;
    public TableColumn<Subject, Byte> colSubjectSemester;

    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final StudentDAO studentDAO = new StudentDAO();

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colStudentEtrCode.setCellValueFactory(new PropertyValueFactory<>("etrCode"));
        colStudentFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colStudentLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colStudentMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        colStudentYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        // subjects
        colSubjectCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colSubjectName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSubjectCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        colSubjectGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colSubjectYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colSubjectSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        tvSubjects.setPlaceholder(new Label("Nincs teljesített kurzus"));
        showStudents();
    }

    public void selectStudent(MouseEvent mouseEvent) {
        Student student = tvStudents.getSelectionModel().getSelectedItem();
        if (student == null)
            return;
        ObservableList<Subject> subjects = FXCollections.observableArrayList(
            subjectDAO.getSubjectsForStudent(student.getEtrCode()));
        tvSubjects.setItems(subjects);
    }

    public void showStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList(studentDAO.getStudents());
        tvStudents.setItems(students);
    }

}
