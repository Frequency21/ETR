package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.stream.IntStream;

public class Testing extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ObservableList<String> data = FXCollections.observableArrayList();
        IntStream.range(0, 1000).mapToObj(Integer::toString).forEach(data::add);

        FilteredList<String> filteredData = new FilteredList<>(data, s -> true);

        Label filterLabel = new Label("Search");
        filterLabel.setTextFill(Color.web("#0076a3"));
        filterLabel.setAlignment(Pos.CENTER);

        TextField filterInput = new TextField();
        filterInput.textProperty().addListener(obs->{
            String filter = filterInput.getText();
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(null);
            }
            else {
                filteredData.setPredicate(s -> s.contains(filter));
            }
        });


        BorderPane content = new BorderPane(new ListView<>(filteredData));
        HBox search = new HBox(filterLabel, filterInput);
        search.setPadding(new Insets(5));
        search.setSpacing(5);
        search.setAlignment(Pos.CENTER_LEFT);
        content.setBottom(search);
//        content.setBottom(filterLabel);
//        content.setBottom(filterInput);

        Scene scene = new Scene(content, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
