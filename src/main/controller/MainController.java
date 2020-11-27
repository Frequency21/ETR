package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainController {

    public StudentsTabController studentsTabController;
    public TeachersTabController teachersTabController;
    public CoursesTabController coursesTabController;
    public LecturesTabController lecturesTabController;
    public AttendanceTabController attendanceTabController;
    public AddAttendanceTabController addAttendanceTabController;
    public Tab students;
    public Tab teachers;
    public Tab courses;
    public Tab lectures;
    public Tab attendance;
    public Tab addAttendance;

    @FXML
    private void initialize() {
        attendanceTabController.injectMainController(this);
        addAttendanceTabController.injectMainController(this);
        attendance.setOnSelectionChanged(event -> {
            attendanceTabController.showStudents();
            attendanceTabController.showLectures();
        });
        addAttendance.setOnSelectionChanged(event -> {
            addAttendanceTabController.showStudents();
            addAttendanceTabController.showLectures();
        });
    }

}
