package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import main.dao.CourseDAO;
import main.model.Course;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RequirementsTabController implements Initializable {

    public TableView<Course> tvCourses;
    public TableColumn<Course, String> colCourseCode;
    public TableColumn<Course, String> colName;
    public TableColumn<Course, Short> colCredit;
    public TableColumn<Course, Boolean> colPractice;
    public TableView<Course> tvDepCourses;
    public TableColumn<Course, String> colDepCourseCode;
    public TableColumn<Course, String> colDepName;
    public TableColumn<Course, Short> colDepCredit;
    public TableColumn<Course, Boolean> colDepPractice;

    private final CourseDAO courseDAO = new CourseDAO();

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        colPractice.setCellValueFactory(new PropertyValueFactory<>("pracitce"));
        colPractice.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "gyakorlat" : "előadás");
            }
        });
        // Course dependencies
        colDepCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colDepName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDepCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        colDepPractice.setCellValueFactory(new PropertyValueFactory<>("pracitce"));
        colDepPractice.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "gyakorlat" : "előadás");
            }
        });
        tvDepCourses.setPlaceholder(new Label("A kurzusnak nincs előfeltétele"));
        showCourses();
    }

    public void selectCourse(MouseEvent mouseEvent) {
        Course course = tvCourses.getSelectionModel().getSelectedItem();
        if (course == null)
            return;
        List<Course> courseList = courseDAO.getDependenciesForCourse(course.getCourseCode());
        if (courseList == null)
            return;
        ObservableList<Course> courses = FXCollections.observableArrayList(courseList);
        tvDepCourses.setItems(courses);
    }

    public void showCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList(courseDAO.getCourses());
        tvCourses.setItems(courses);
    }

}
