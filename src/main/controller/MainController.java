package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainController {

    public StudentsTabController studentsTabController;
    public TeachersTabController teachersTabController;
    public CoursesTabController coursesTabController;
    public LecturesTabController lecturesTabController;
    public AddAttendanceTabController addAttendanceTabController;
    public SubjectTabController subjectTabController;
    public GradePointAverageController gradePointAveragesTabController;
    public TeachesTabController teachesTabController;
    public Tab students;
    public Tab teachers;
    public Tab courses;
    public Tab lectures;
    public Tab addAttendances;
    public Tab subjects;
    public Tab gradePointAverages;
    public Tab teaches;

    @FXML
    private void initialize() {
        addAttendances.setOnSelectionChanged(event -> {
            addAttendanceTabController.showStudents();
            addAttendanceTabController.showLectures();
        });
        subjects.setOnSelectionChanged(event -> {
            subjectTabController.showStudents();
            subjectTabController.showStudents();
        });
        gradePointAverages.setOnSelectionChanged(event -> {
            gradePointAveragesTabController.showGPAs();
        });
        teaches.setOnSelectionChanged(event -> {
            teachesTabController.showTeaches();
        });
    }

}
