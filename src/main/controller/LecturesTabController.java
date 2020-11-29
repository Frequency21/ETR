package main.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
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
    private final SpinnerValueFactory<LocalTime> spvfBegin = new DateSpinnerValueFactory();
    private final SpinnerValueFactory<LocalTime> spvfEnd = new DateSpinnerValueFactory();
    private final SpinnerValueFactory<LocalTime> spvfBeginTo = new DateSpinnerValueFactory();
    private final SpinnerValueFactory<LocalTime> spvfEndTo = new DateSpinnerValueFactory();

    public SplitPane splitPane;
    public Button btnWhich;
    public Button btnTo;
    public TextField tfCourseCodeTo;
    public TextField tfYearTo;
    private final ToggleGroup toggleGroupTo = new ToggleGroup();
    public RadioButton rbSemester1To;
    public RadioButton rbSemester2To;
    public ChoiceBox<String> cbDayTo;
    public Spinner<LocalTime> spBeginTo;
    public Spinner<LocalTime> spEndTo;
    public TextField tfMaxTo;
    public TextField tfBuildingCodeTo;
    public TextField tfRoomCodeTo;
    public Button btnUpdateTo;

    public LecturesTabController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbDay.getItems().addAll("hétfő", "kedd", "szerda", "csütörtök", "péntek");
        cbDayTo.getItems().addAll("hétfő", "kedd", "szerda", "csütörtök", "péntek");
        toggleGroup.getToggles().add(rbSemester1);
        toggleGroup.getToggles().add(rbSemester2);
        toggleGroupTo.getToggles().add(rbSemester1To);
        toggleGroupTo.getToggles().add(rbSemester2To);
        rbSemester1.setSelected(true);
        rbSemester1To.setSelected(true);
        // add valueFactory for spinners
        spBegin.setValueFactory(spvfBegin);
        spEnd.setValueFactory(spvfEnd);
        spvfBegin.setValue(LocalTime.of(8, 0));
        spvfEnd.setValue(LocalTime.of(8, 0));
        spBeginTo.setValueFactory(spvfBeginTo);
        spEndTo.setValueFactory(spvfEndTo);
        spvfBeginTo.setValue(LocalTime.of(8, 0));
        spvfEndTo.setValue(LocalTime.of(8, 0));
        // initialize cols
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colMax.setCellValueFactory(new PropertyValueFactory<>("max"));
        colBuildingCode.setCellValueFactory(new PropertyValueFactory<>("buildingCode"));
        colRoomCode.setCellValueFactory(new PropertyValueFactory<>("roomCode"));
        showLectures();

        // splitpane
        btnWhich.setOnAction(e -> {
            KeyValue keyValue = new KeyValue(splitPane.getDividers().get(0).positionProperty(), 0);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), keyValue));
            timeline.play();
        });

        btnTo.setOnAction(e -> {
            KeyValue keyValue = new KeyValue(splitPane.getDividers().get(0).positionProperty(), 1);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), keyValue));
            timeline.play();
        });

    }

    private void showLectures() {
        ObservableList<Lecture> lectures = FXCollections.observableArrayList(lectureDAO.getLectures());
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
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input hiba!");
            alert.setHeaderText("Az év / max létszám értéke csak szám lehet");
            alert.show();
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
        try {
            lectureDAO.updateLecture(Short.parseShort(tfYearTo.getText()), (byte) (rbSemester1To.isSelected() ? 1 : 2),
                cbDayTo.getValue(), spBeginTo.getValue(), spEndTo.getValue(), Short.parseShort(tfMaxTo.getText()),
                tfCourseCodeTo.getText(), tfBuildingCodeTo.getText(), tfRoomCodeTo.getText(),
                Short.parseShort(tfYear.getText()), (byte) (rbSemester1.isSelected() ? 1 : 2),
                cbDay.getValue(), spBegin.getValue(), tfBuildingCode.getText(), tfRoomCode.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input hiba!");
            alert.setHeaderText("Az év / max létszám értéke csak szám lehet");
            alert.show();
        }
//        clear();
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
        try {
            lectureDAO.deleteLecture(Short.parseShort(tfYear.getText()), (byte) (rbSemester1.isSelected() ? 1 : 2),
                cbDay.getValue(), spBegin.getValue(), tfBuildingCode.getText(), tfRoomCode.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input hiba!");
            alert.setHeaderText("Az év értéke csak szám lehet");
            alert.show();
        }
        clear();
        showLectures();
    }

    public void selectRecord(MouseEvent mouseEvent) {
        Lecture lecture = tvLectures.getSelectionModel().getSelectedItem();
        // enélkül null pointer exception keletkezik,
        // ha valamelyik oszlop szerint rendezünk
        if (lecture == null)
            return;
        setFields(lecture, tfCourseCode, tfYear, rbSemester1, rbSemester2, cbDay, spvfBegin,
            spvfEnd, tfMax, tfBuildingCode, tfRoomCode);
        // same for other pane
        setFields(lecture, tfCourseCodeTo, tfYearTo, rbSemester1To, rbSemester2To, cbDayTo,
            spvfBeginTo, spvfEndTo, tfMaxTo, tfBuildingCodeTo, tfRoomCodeTo);
    }

    private void setFields(Lecture lecture, TextField tfCourseCode, TextField tfYear, RadioButton rbSemester1,
                           RadioButton rbSemester2, ChoiceBox<String> cbDay, SpinnerValueFactory<LocalTime> spvfBegin,
                           SpinnerValueFactory<LocalTime> spvfEnd, TextField tfMax, TextField tfBuildingCode, TextField tfRoomCode) {
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

    @SuppressWarnings("DuplicatedCode")
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
        tfCourseCodeTo.clear();
        tfYearTo.clear();
        rbSemester1To.setSelected(false);
        rbSemester2To.setSelected(false);
        cbDayTo.setValue(null);
        spvfBeginTo.setValue(null);
        spvfEndTo.setValue(null);
        tfMaxTo.clear();
        tfBuildingCodeTo.clear();
        tfRoomCodeTo.clear();
    }

}

class DateSpinnerValueFactory extends SpinnerValueFactory<LocalTime> {
    {setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"), null));}

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
}