package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.dao.LectureDAO;
import main.dao.StudentDAO;
import main.exceptions.MissingRequirementException;
import main.model.Lecture;
import main.model.Student;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AttendanceTabController implements Initializable {

    public TableView<Student> tvStudents;
    public TableColumn<Student, String> colStudentEtrCode;
    public TableColumn<Student, String> colStudentFirstName;
    public TableColumn<Student, String> colStudentLastName;
    public TableColumn<Student, Short> colStudentYear;
    public TableColumn<Student, String> colStudentMajor;

    public TableView<Lecture> tvLectures;
    public TableColumn<Lecture, String> colLectureCourseCode;
    public TableColumn<Lecture, Short> colLectureYear;
    public TableColumn<Lecture, Byte> colLectureSemester;
    public TableColumn<Lecture, String> colLectureDay;
    public TableColumn<Lecture, Date> colLectureBegin;
    public TableColumn<Lecture, Date> colLectureEnd;
    public TableColumn<Lecture, Short> colLectureMax;
    public TableColumn<Lecture, String> colLectureBuildingCode;
    public TableColumn<Lecture, String> colLectureRoomCode;

    private final LectureDAO lectureDAO = new LectureDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private MainController mainController;

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // student
        colStudentEtrCode.setCellValueFactory(new PropertyValueFactory<>("etrCode"));
        colStudentFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colStudentLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colStudentMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        colStudentYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        // lecture
        colLectureCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colLectureYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colLectureSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colLectureDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colLectureBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        colLectureEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colLectureMax.setCellValueFactory(new PropertyValueFactory<>("max"));
        colLectureBuildingCode.setCellValueFactory(new PropertyValueFactory<>("buildingCode"));
        colLectureRoomCode.setCellValueFactory(new PropertyValueFactory<>("roomCode"));
        showLectures();
        showStudents();
    }

    @SuppressWarnings("DuplicatedCode")
    void showLectures() {
        ObservableList<Lecture> lectures = FXCollections.observableArrayList(lectureDAO.getLectures());
        tvLectures.setItems(lectures);
    }

    void showStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList(studentDAO.getStudents());
        tvStudents.setItems(students);
    }

}
