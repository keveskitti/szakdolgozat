package com.mygame.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Print the file path to check if it's correct
            System.out.println("FXML file path: " + getClass().getResource("/sudoku.fxml"));

            // Load FXML file and set the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sudoku.fxml"));
            BorderPane root = loader.load(); // Ensure it's loading correctly

            Scene scene = new Scene(root);
            primaryStage.setTitle("Sudoku");
            primaryStage.setScene(scene);
            primaryStage.show();

            System.out.println("FXML file loaded and window displayed.");
        } catch (Exception e) {
            System.out.println("Error occurred while loading FXML:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
