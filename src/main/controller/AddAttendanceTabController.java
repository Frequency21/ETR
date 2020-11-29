package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.dao.AttendanceDAO;
import main.dao.LectureDAO;
import main.dao.StudentDAO;
import main.exceptions.MissingRequirementException;
import main.model.Lecture;
import main.model.Student;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddAttendanceTabController implements Initializable {
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
    public CheckBox cbIsDone;
    public Spinner<Integer> spGrade;

    public Button btnInsertRecord;
    public Button btnDeleteRecord;
    public Button btnUpdateRecord;

    private final LectureDAO lectureDAO = new LectureDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbIsDone.setSelected(true);
        spGrade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5));
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

    public void addLectureToStudent(ActionEvent mouseEvent) {
        Student student = tvStudents.getSelectionModel().getSelectedItem();
        Lecture lecture = tvLectures.getSelectionModel().getSelectedItem();
        if (student == null || lecture == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sikertelen teljesítés felvétel!");
            alert.setHeaderText("Nincs hallgató vagy tanóra kiválasztva");
            alert.show();
            return;
        }
        try {
            if (cbIsDone.isSelected()) {
                attendanceDAO.addAttendanceWithGrade(Byte.parseByte(spGrade.getValue().toString()), student.getEtrCode(), lecture.getYear(),
                    lecture.getSemester(), lecture.getDay(), lecture.getBegin(), lecture.getBuildingCode(),
                    lecture.getRoomCode());
                return;
            }
            attendanceDAO.addAttendance(student.getEtrCode(), lecture.getYear(), lecture.getSemester(),
                lecture.getDay(), lecture.getBegin(), lecture.getBuildingCode(), lecture.getRoomCode());
        } catch (MissingRequirementException mrex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sikertelen kurzusfelvétel!");
            alert.setHeaderText("Az alábi kurzus előfeltételek nem teljesülnek: " +
                mrex.getCourseCodes().stream().collect(Collectors.joining("\n    - ", "\n    - ", "")));
            alert.show();
        }
        showStudents();
        showLectures();
    }

    @SuppressWarnings("DuplicatedCode")
    public void removeLectureForStudent(ActionEvent mouseEvent) {
        Student student = tvStudents.getSelectionModel().getSelectedItem();
        Lecture lecture = tvLectures.getSelectionModel().getSelectedItem();
        if (student == null || lecture == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sikertelen teljesítés felvétel!");
            alert.setHeaderText("Nincs hallgató vagy tanóra kiválasztva");
            alert.show();
            return;
        }
        attendanceDAO.deleteAttendance(student.getEtrCode(), lecture.getYear(),
            lecture.getSemester(), lecture.getDay(), lecture.getBegin());
        showStudents();
        showLectures();
    }

    public void updateLectureForStudent(ActionEvent mouseEvent) {
        Student student = tvStudents.getSelectionModel().getSelectedItem();
        Lecture lecture = tvLectures.getSelectionModel().getSelectedItem();
        showStudents();
        showLectures();
        if (student == null || lecture == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sikertelen teljesítés felvétel!");
            alert.setHeaderText("Nincs hallgató vagy tanóra kiválasztva");
            alert.show();
            return;
        }
        if (cbIsDone.isSelected()) {
            attendanceDAO.updateAttendanceWithGrade(Byte.parseByte(spGrade.getValue().toString()), student.getEtrCode(),
                lecture.getYear(), lecture.getSemester(), lecture.getDay(), lecture.getBegin());
            return;
        }
        attendanceDAO.updateAttendance(student.getEtrCode(), lecture.getYear(), lecture.getSemester(),
            lecture.getDay(), lecture.getBegin());
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

    @FXML
    private void toggleEditable(ActionEvent event) {
        spGrade.setDisable(!cbIsDone.isSelected());
    }

}
