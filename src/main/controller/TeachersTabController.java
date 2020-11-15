package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.dao.TeacherDAO;
import main.model.Teacher;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class TeachersTabController implements Initializable {
    public TextField tfEtrCode;
    public TextField tfFirstName;
    public TextField tfLastName;
    public TextField tfDepartment;
    public TextField tfEmail;
    public Button btnInsert;
    public Button btnUpdate;
    public Button btnDelete;
    public TableView<Teacher> tvTeachers;
    public TableColumn<Teacher, String> colEtr;
    public TableColumn<Teacher, String> colFirstName;
    public TableColumn<Teacher, String> colLastName;
    public TableColumn<Teacher, String> colDepartment;
    public TableColumn<Teacher, String> colEmail;

    private final TeacherDAO teacherDAO = new TeacherDAO();

    public TeachersTabController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showTeachers();
    }

    void showTeachers() {
        ObservableList<Teacher> teachers = FXCollections.observableArrayList(teacherDAO.getTeachers());
        colEtr.setCellValueFactory(new PropertyValueFactory<>("etrCode"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tvTeachers.setItems(teachers);
    }

    public void insertRecord(ActionEvent event) {
        if (tfFirstName.getText().isEmpty() ||
            tfLastName.getText().isEmpty() ||
            tfDepartment.getText().isEmpty() ||
            tfEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Az ETR-kód mező kivételével egyik mező sem lehet üres" +
                " új oktató felvételénél.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        } else if (tfFirstName.getText().length() < 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A vezetéknévnek legalább két karakter hosszúnak kell lennie.");
            alert.setHeaderText("Hibás vezetéknév!");
            alert.show();
            return;
        }
        try {
            teacherDAO.addTeacher(tfFirstName.getText(), tfLastName.getText(), tfDepartment.getText(),
                tfEmail.getText());
        } catch (SQLIntegrityConstraintViolationException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oktató felvétele");
            alert.setHeaderText("Az email cím már regisztrálva van a rendszerben");
            alert.show();
            return;
        }
        clear();
        showTeachers();
    }

    public void updateRecord(ActionEvent event) {
        if (tfEtrCode.getText().isEmpty() ||
            tfFirstName.getText().isEmpty() ||
            tfLastName.getText().isEmpty() ||
            tfDepartment.getText().isEmpty() ||
            tfEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Egyik mező sem lehet üres oktató adatainak frissítésénél.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        teacherDAO.updateTeacher(tfEtrCode.getText(), tfFirstName.getText(), tfLastName.getText(),
            tfDepartment.getText(), tfEmail.getText());
        clear();
        showTeachers();
    }

    public void deleteRecord(ActionEvent event) {
        if (tfEtrCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Törléshez add meg az oktató ETR kódját.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        teacherDAO.deleteTeacher(tfEtrCode.getText());
        clear();
        showTeachers();
    }

    public void selectRecord(MouseEvent mouseEvent) {
        Teacher teacher = tvTeachers.getSelectionModel().getSelectedItem();
        if (teacher == null)
            return;
        tfEtrCode.setText(teacher.getEtrCode());
        tfFirstName.setText(teacher.getFirstName());
        tfLastName.setText(teacher.getLastName());
        tfDepartment.setText(teacher.getDepartment());
        tfEmail.setText(teacher.getEmail());
    }

    private void clear() {
        tfEtrCode.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfDepartment.clear();
        tfEmail.clear();
    }
}
