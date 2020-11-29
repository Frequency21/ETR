package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.dao.CourseDAO;
import main.model.Course;
import main.model.Student;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ResourceBundle;

public class CoursesTabController implements Initializable {

    private final CourseDAO courseDAO = new CourseDAO();

    public TextField tfEtrCode;
    public Button btnInsert;
    public Button btnUpdate;
    public Button btnDelete;
    public TableView<Course> tvCourses;
    public TextField tfCourseCode;
    public TextField tfName;
    public TextField tfCredit;
    public CheckBox cbPractice;
    public TableColumn<Course, String> colCourseCode;
    public TableColumn<Course, String> colName;
    public TableColumn<Course, Integer> colCredit;
    public TableColumn<Course, Boolean> colPractice;
    public TableColumn<Course, String> colEtrCode;


    public CoursesTabController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        colPractice.setCellValueFactory(new PropertyValueFactory<>("pracitce"));
        colPractice.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "gyakorlat" : "előadás");
            }
        });
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEtrCode.setCellValueFactory(new PropertyValueFactory<>("etrCode"));
        showCourses();
    }

    void showCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList(courseDAO.getCourses());
        tvCourses.setItems(courses);
    }

    public void insertRecord(ActionEvent event) {
        if (tfCourseCode.getText().isEmpty() ||
            tfName.getText().isEmpty() ||
            tfCredit.getText().isEmpty() ||
            tfEtrCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Egyik mező sem lehet üres" +
                " új kurzus felvételénél.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }

        try {
            courseDAO.addCourse(tfCourseCode.getText(), Short.parseShort(tfCredit.getText()),
                cbPractice.isSelected(), tfName.getText(), tfEtrCode.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input hiba!");
            alert.setHeaderText("A kredit értéke csak szám lehet");
            alert.show();
        }

        clear();
        showCourses();
    }

    public void updateRecord(ActionEvent event) {
        if (tfCourseCode.getText().isEmpty() ||
            tfName.getText().isEmpty() ||
            tfCredit.getText().isEmpty() ||
            tfEtrCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Egyik mező sem lehet üres kurzus adatainak frissítésénél.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        try {
            courseDAO.updateCourse(tfCourseCode.getText(), Short.parseShort(tfCredit.getText()), cbPractice.isSelected(),
                tfName.getText(), tfEtrCode.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input hiba!");
            alert.setHeaderText("A kredit értéke csak szám lehet");
            alert.show();
        }
//        clear();
        showCourses();
    }

    public void deleteRecord(ActionEvent event) {
        if (tfEtrCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Törléshez add meg a kurzus kurzuskódját!");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        courseDAO.deleteCourse(tfCourseCode.getText());
        clear();
        showCourses();
    }

    public void selectRecord(MouseEvent mouseEvent) {
        Course course = tvCourses.getSelectionModel().getSelectedItem();
        if (course == null)
            return;
        tfCourseCode.setText(course.getCourseCode());
        tfCredit.setText("" + course.getCredit());
        tfName.setText(course.getName());
        cbPractice.setSelected(course.isPracitce());
        tfEtrCode.setText(course.getEtrCode());
    }

    private void clear() {
        tfCourseCode.clear();
        tfCredit.clear();
        tfName.clear();
        cbPractice.setSelected(false);
        tfEtrCode.clear();
    }
}
