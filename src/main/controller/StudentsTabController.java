package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.dao.StudentDAO;
import main.model.Student;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class StudentsTabController implements Initializable {

    private final StudentDAO studentDAO = new StudentDAO();

    public TextField tfEtrCode;
    public TextField tfFirstName;
    public TextField tfLastName;
    public TextField tfMajor;
    public TextField tfYear;
    public TextField tfEmail;
    public Button btnInsert;
    public Button btnUpdate;
    public Button btnDelete;
    public TableView<Student> tvStudents;
    public TableColumn<Student, String> colEtr;
    public TableColumn<Student, String> colFirstName;
    public TableColumn<Student, String> colLastName;
    public TableColumn<Student, String> colMajor;
    public TableColumn<Student, Short> colYear;
    public TableColumn<Student, String> colEmail;

    public StudentsTabController() {}

    @FXML
    public void handleButtonEvent(ActionEvent event) {
        if (tfFirstName.getText() != null && tfFirstName.getText().equals("easter egg")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You found the easter egg.");
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showStudents();
    }

    void showStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList(studentDAO.getStudents());
        colEtr.setCellValueFactory(new PropertyValueFactory<>("etrCode"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tvStudents.setItems(students);
    }

    /*
    etr_kod CHAR(11) PRIMARY KEY,
    vnev    VARCHAR(20)  NOT NULL,
    knev    VARCHAR(50)  NOT NULL
    szak     VARCHAR(100) NOT NULL,
    evfolyam SMALLINT     NOT NULL,,
    email   VARCHAR(100) NOT NULL UNIQUE
     */

    public void insertRecord(ActionEvent event) {
        if (tfFirstName.getText().isEmpty() ||
            tfLastName.getText().isEmpty() ||
            tfMajor.getText().isEmpty() ||
            tfYear.getText().isEmpty() ||
            tfEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Az ETR-kód mező kivételével egyik mező sem lehet üres" +
                " új hallgató felvételénél.");
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
            studentDAO.addStudent(tfFirstName.getText(), tfLastName.getText(), tfMajor.getText(),
                Short.parseShort(tfYear.getText()), tfEmail.getText());
        } catch (SQLIntegrityConstraintViolationException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hallgató felvétele");
            alert.setHeaderText("Az email cím már regisztrálva van a rendszerben");
            alert.show();
            return;
        }
        clear();
        showStudents();
    }

    public void updateRecord(ActionEvent event) {
        if (tfEtrCode.getText().isEmpty() ||
            tfFirstName.getText().isEmpty() ||
            tfLastName.getText().isEmpty() ||
            tfMajor.getText().isEmpty() ||
            tfYear.getText().isEmpty() ||
            tfEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Egyik mező sem lehet üres hallgató adatainak frissítésénél.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        studentDAO.updateStudent(tfEtrCode.getText(), tfFirstName.getText(), tfLastName.getText(), tfMajor.getText(),
            Short.parseShort(tfYear.getText()), tfEmail.getText());
//        clear();
        showStudents();
    }

    public void deleteRecord(ActionEvent event) {
        if (tfEtrCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Törléshez add meg a hallgató ETR kódját.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        studentDAO.deleteStudent(tfEtrCode.getText());
        clear();
        showStudents();
    }

    public void selectRecord(MouseEvent mouseEvent) {
        Student student = tvStudents.getSelectionModel().getSelectedItem();
        if (student == null)
            return;
        tfEtrCode.setText(student.getEtrCode());
        tfFirstName.setText(student.getFirstName());
        tfLastName.setText(student.getLastName());
        tfMajor.setText(student.getMajor());
        tfYear.setText("" + student.getYear());
        tfEmail.setText(student.getEmail());
    }

    private void clear() {
        tfEtrCode.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfMajor.clear();
        tfYear.clear();
        tfEmail.clear();
    }
}
