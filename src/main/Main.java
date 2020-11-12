package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent window = FXMLLoader.load(getClass().getResource("view/Main.fxml"));
        Scene scene = new Scene(window);
        primaryStage.setTitle("Egyetemi Tanulmányi Rendszer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
