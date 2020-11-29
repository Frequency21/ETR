package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.LectureRoom;
import main.dao.LectureRoomDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class LectureRoomTabController implements Initializable {

    public TableView<LectureRoom> tvLectureRooms;
    public TableColumn<LectureRoom, String> colBuildingCode;
    public TableColumn<LectureRoom, String> colRoomCode;
    public TableColumn<LectureRoom, Boolean> colCabinet;
    public TableColumn<LectureRoom, Short> colMax;

    private final LectureRoomDAO lectureRoomDAO = new LectureRoomDAO();

    public LectureRoomTabController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colBuildingCode.setCellValueFactory(new PropertyValueFactory<>("buildingCode"));
        colRoomCode.setCellValueFactory(new PropertyValueFactory<>("roomCode"));
        colCabinet.setCellValueFactory(new PropertyValueFactory<>("cabinet"));
        colCabinet.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "igen" : "nem");
            }
        });
        colMax.setCellValueFactory(new PropertyValueFactory<>("max"));
        showLectureRooms();
    }

    private void showLectureRooms() {
        ObservableList<LectureRoom> lectureRooms = FXCollections.observableArrayList(lectureRoomDAO.getLectureRooms());
        tvLectureRooms.setItems(lectureRooms);
    }

}
