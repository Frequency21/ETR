package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.dao.LectureRoomStatisticDAO;
import main.model.Lecture;
import main.model.LectureRoomStatistic;

import java.net.URL;
import java.util.ResourceBundle;

public class LectureRoomStatisticController implements Initializable {

    public TableView<LectureRoomStatistic> tvStatistics;
    public TableColumn<LectureRoomStatistic, Short> colYear;
    public TableColumn<LectureRoomStatistic, Byte> colSemester;
    public TableColumn<LectureRoomStatistic, String> colBuildingCode;
    public TableColumn<LectureRoomStatistic, String> colRoomCode;
    public TableColumn<LectureRoomStatistic, Boolean> colCabinet;
    public TableColumn<LectureRoomStatistic, Short> colMax;
    public TableColumn<LectureRoomStatistic, Integer> colQuantity;

    private final LectureRoomStatisticDAO lrsDAO = new LectureRoomStatisticDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colYear.setCellValueFactory(new PropertyValueFactory<>("Year"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("Semester"));
        colBuildingCode.setCellValueFactory(new PropertyValueFactory<>("BuildingCode"));
        colRoomCode.setCellValueFactory(new PropertyValueFactory<>("RoomCode"));
        colCabinet.setCellValueFactory(new PropertyValueFactory<>("Cabinet"));
        colCabinet.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "igen" : "nem");
            }
        });
        colMax.setCellValueFactory(new PropertyValueFactory<>("Max"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        showStatistics();
    }

    public void showStatistics() {
        ObservableList<LectureRoomStatistic> statistics = FXCollections.observableArrayList(lrsDAO.getStatistics());
        tvStatistics.setItems(statistics);
    }
}
