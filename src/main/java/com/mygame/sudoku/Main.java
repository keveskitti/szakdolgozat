package com.mygame.sudoku;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        SudokuGame game = new SudokuGame();
        Scene gameScene = game.createGameScene(primaryStage);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

