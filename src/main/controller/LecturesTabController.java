package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.LocalTimeStringConverter;
import main.dao.LectureDAO;
import main.model.Lecture;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class LecturesTabController implements Initializable {

    private final LectureDAO lectureDAO = new LectureDAO();

    public TextField tfCourseCode;
    public TextField tfYear;
    public RadioButton rbSemester1;
    public RadioButton rbSemester2;
    public ChoiceBox<String> cbDay;
    public Spinner<LocalTime> spBegin;
    public Spinner<LocalTime> spEnd;
    public TextField tfMax;
    public TextField tfBuildingCode;
    public TextField tfRoomCode;
    public Button btnInsert;
    public Button btnUpdate;
    public Button btnDelete;
    public TableView<Lecture> tvLectures;

    public TableColumn<Lecture, String> colCourseCode;
    public TableColumn<Lecture, Short> colYear;
    public TableColumn<Lecture, Byte> colSemester;
    public TableColumn<Lecture, String> colDay;
    public TableColumn<Lecture, Date> colBegin;
    public TableColumn<Lecture, Date> colEnd;
    public TableColumn<Lecture, Short> colMax;
    public TableColumn<Lecture, String> colBuildingCode;
    public TableColumn<Lecture, String> colRoomCode;

    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private final SpinnerValueFactory<LocalTime> spvfBegin = new SpinnerValueFactory<>() {
        {setConverter(new LocalTimeStringConverter(formatter, null));}

        @Override
        public void decrement(int steps) {
            if (getValue() == null) setValue(LocalTime.of(8, 0));
            else {
                LocalTime time = getValue();
                if (time.getHour() == 8)
                    return;
                setValue(time.minusHours(steps));
            }
        }

        @Override
        public void increment(int steps) {
            if (this.getValue() == null) setValue(LocalTime.of(8, 0));
            else {
                LocalTime time = getValue();
                if (time.getHour() == 20)
                    return;
                setValue(time.plusHours(steps));
            }
        }
    };
    private final SpinnerValueFactory<LocalTime> spvfEnd = new SpinnerValueFactory<>() {
        {setConverter(new LocalTimeStringConverter(formatter, null));}

        @Override
        public void decrement(int steps) {
            if (getValue() == null) setValue(LocalTime.of(8, 0));
            else {
                LocalTime time = getValue();
                if (time.getHour() == 8)
                    return;
                setValue(time.minusHours(steps));
            }
        }

        @Override
        public void increment(int steps) {
            if (this.getValue() == null) setValue(LocalTime.of(8, 0));
            else {
                LocalTime time = getValue();
                if (time.getHour() == 20)
                    return;
                setValue(time.plusHours(steps));
            }
        }
    };

    public LecturesTabController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbDay.getItems().addAll("hétfő", "kedd", "szerda", "csütörtök", "péntek");
        toggleGroup.getToggles().add(rbSemester1);
        toggleGroup.getToggles().add(rbSemester2);
        rbSemester1.setSelected(true);
        // add valueFactory for spinners
        spBegin.setValueFactory(spvfBegin);
        spEnd.setValueFactory(spvfEnd);
        spvfBegin.setValue(LocalTime.of(8, 0));
        spvfEnd.setValue(LocalTime.of(8, 0));
        showLectures();
    }

    void showLectures() {
        ObservableList<Lecture> lectures = FXCollections.observableArrayList(lectureDAO.getLectures());
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colMax.setCellValueFactory(new PropertyValueFactory<>("max"));
        colBuildingCode.setCellValueFactory(new PropertyValueFactory<>("buildingCode"));
        colRoomCode.setCellValueFactory(new PropertyValueFactory<>("roomCode"));
        tvLectures.setItems(lectures);
    }

    public void insertRecord(ActionEvent event) {
        if (tfCourseCode.getText().isEmpty() ||
            tfYear.getText().isEmpty() ||
            (!rbSemester1.isSelected() &&
                !rbSemester2.isSelected()) ||
            cbDay.getValue().isEmpty() ||
            spBegin.getValue() == null ||
            spEnd.getValue() == null ||
            tfMax.getText().isEmpty() ||
            tfBuildingCode.getText().isEmpty() ||
            tfRoomCode.getText().isEmpty()
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Egyik mező sem lehet üres tanóra felvitelénél!");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        } else if (spBegin.getValue().isAfter(spEnd.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "A kezdés időpntjának előbbre kell esnie a végzés időpontjánál");
            alert.setHeaderText("Nem megfelelő időpontok!");
            alert.show();
            return;
        }
        try {
            lectureDAO.addLecture(Short.parseShort(tfYear.getText()), (byte) (rbSemester1.isSelected() ? 1 : 2),
                cbDay.getValue(), spBegin.getValue(), spEnd.getValue(), Short.parseShort(tfMax.getText()),
                tfCourseCode.getText(), tfBuildingCode.getText(), tfRoomCode.getText());
        } catch (SQLIntegrityConstraintViolationException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tanóra felvétele");
            alert.setHeaderText("");
            alert.show();
            return;
        }
        clear();
        showLectures();
    }

    public void updateRecord(ActionEvent event) {
        if (tfCourseCode.getText().isEmpty() ||
            tfYear.getText().isEmpty() ||
            (!rbSemester1.isSelected() &&
                !rbSemester2.isSelected()) ||
            cbDay.getValue().isEmpty() ||
            spBegin.getValue() == null ||
            spEnd.getValue() == null ||
            tfMax.getText().isEmpty() ||
            tfBuildingCode.getText().isEmpty() ||
            tfRoomCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Egyik mező sem lehet üres tanóra adatainak frissítésénél.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        lectureDAO.updateLecture(Short.parseShort(tfYear.getText()), (byte) (rbSemester1.isSelected() ? 1 : 2),
            cbDay.getValue(), spBegin.getValue(), spEnd.getValue(), Short.parseShort(tfMax.getText()),
            tfCourseCode.getText(), tfBuildingCode.getText(), tfRoomCode.getText());
        clear();
        showLectures();
    }

    //    short year, byte semester, String day, LocalTime begin, String buildingCode, String roomCode)
    public void deleteRecord(ActionEvent event) {
        if (tfYear.getText().isEmpty() ||
            (!rbSemester1.isSelected() &&
                !rbSemester2.isSelected()) ||
            cbDay.getValue().isEmpty() ||
            spBegin.getValue() == null ||
            tfBuildingCode.getText().isEmpty() ||
            tfRoomCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Törléshez add meg a tanóra év, félév, nap, kezdés," +
                " épület- és teremkód adatait.");
            alert.setHeaderText("Hiányzó adatok!");
            alert.show();
            return;
        }
        lectureDAO.deleteLecture(Short.parseShort(tfYear.getText()), (byte) (rbSemester1.isSelected() ? 1 : 2),
            cbDay.getValue(), spBegin.getValue(), tfBuildingCode.getText(), tfRoomCode.getText());
        clear();
        showLectures();
    }

    public void selectRecord(MouseEvent mouseEvent) {
        Lecture lecture = tvLectures.getSelectionModel().getSelectedItem();
        tfCourseCode.setText(lecture.getCourseCode());
        tfYear.setText("" + lecture.getYear());
        if (lecture.getSemester() == 1)
            rbSemester1.setSelected(true);
        else
            rbSemester2.setSelected(true);
        cbDay.setValue(lecture.getDay());
        spvfBegin.setValue(lecture.getBegin());
        spvfEnd.setValue(lecture.getEnd());
        tfMax.setText("" + lecture.getMax());
        tfBuildingCode.setText(lecture.getBuildingCode());
        tfRoomCode.setText(lecture.getRoomCode());
    }

    private void clear() {
        tfCourseCode.clear();
        tfYear.clear();
        rbSemester1.setSelected(false);
        rbSemester2.setSelected(false);
        cbDay.setValue(null);
        spvfBegin.setValue(null);
        spvfEnd.setValue(null);
        tfMax.clear();
        tfBuildingCode.clear();
        tfRoomCode.clear();
    }

}
