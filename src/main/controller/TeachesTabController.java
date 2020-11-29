package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.dao.TeachesDAO;
import main.model.LectureRoom;
import main.model.Teaches;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class TeachesTabController implements Initializable {
    public TableView<Teaches> tvTeaches;
    public TableColumn<Teaches, String> colEtrCode;
    public TableColumn<Teaches, String> colFirstName;
    public TableColumn<Teaches, String> colLastName;
    public TableColumn<Teaches, String> colCourseCode;
    public TableColumn<Teaches, String> colCourseName;
    public TableColumn<Teaches, Short> colYear;
    public TableColumn<Teaches, Byte> colSemester;
    public TableColumn<Teaches, String> colDay;
    public TableColumn<Teaches, LocalTime> colBegin;
    public TableColumn<Teaches, LocalTime> colEnd;
    public TableColumn<Teaches, String> colBuildingCode;
    public TableColumn<Teaches, String> colRoomCode;

    private final TeachesDAO teachesDAO = new TeachesDAO();

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEtrCode.setCellValueFactory(new PropertyValueFactory<>("etrCode"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colBuildingCode.setCellValueFactory(new PropertyValueFactory<>("buildingCode"));
        colRoomCode.setCellValueFactory(new PropertyValueFactory<>("roomCode"));
    }

    public void showTeaches() {
        ObservableList<Teaches> teaches = FXCollections.observableArrayList(teachesDAO.getTeaches());
        tvTeaches.setItems(teaches);
    }
}
