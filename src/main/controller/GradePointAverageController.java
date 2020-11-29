package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.dao.GradePointAverageDAO;
import main.model.GradePointAverage;

import java.net.URL;
import java.util.ResourceBundle;

public class GradePointAverageController implements Initializable {

    public TableView<GradePointAverage> tvGradeAveragePoints;
    public TableColumn<GradePointAverage, String> colEtrCode;
    public TableColumn<GradePointAverage, String> colFirstName;
    public TableColumn<GradePointAverage, String> colLastName;
    public TableColumn<GradePointAverage, Short> colYear;
    public TableColumn<GradePointAverage, Byte> colSemester;
    public TableColumn<GradePointAverage, Double> colAverage;
    public TableColumn<GradePointAverage, Double> colKki;
    public TableColumn<GradePointAverage, Integer> colCreditSum;

    private final GradePointAverageDAO gpaDAO = new GradePointAverageDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEtrCode.setCellValueFactory(new PropertyValueFactory<>("etrCode"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colAverage.setCellValueFactory(new PropertyValueFactory<>("average"));
        colAverage.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.format("%.2f", item));
            }
        });
        colKki.setCellValueFactory(new PropertyValueFactory<>("kki"));
        colKki.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.format("%.2f", item));
            }
        });
        colCreditSum.setCellValueFactory(new PropertyValueFactory<>("creditSum"));
        showGPAs();
    }

    public void showGPAs() {
        ObservableList<GradePointAverage> gpas = FXCollections.observableArrayList(gpaDAO.getGPAs());
        tvGradeAveragePoints.setItems(gpas);
    }
}
