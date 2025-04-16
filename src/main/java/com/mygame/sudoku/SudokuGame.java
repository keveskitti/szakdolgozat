// UI, játék logikája, időzítő, eredmények
package com.mygame.sudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.List;

public class SudokuGame {

    // segédfüggvény: idő átalakítása másodperccé a sorrendezéshez
    private int timeToSeconds(String time) {
        String[] parts = time.split(":");
        int h = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        int s = Integer.parseInt(parts[2]);
        return h * 3600 + m * 60 + s;
    }

    private int[][] board;
    private IntegerProperty seconds = new SimpleIntegerProperty(0);
    private IntegerProperty minutes = new SimpleIntegerProperty(0);
    private IntegerProperty hours = new SimpleIntegerProperty(0);
    private boolean isGameRunning = false;
    private Timeline timerTimeline;
    private ScoreManager scoreManager = new ScoreManager();
    private Label timerLabel;

    public SudokuGame() {
        board = new int[9][9]; // sudoku tábla létrehozása
    }

    public Scene createGameScene(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        layout.setPrefHeight(700);

        Button startButton = new Button("Indítás");
        layout.getChildren().add(startButton);

        startButton.setOnAction(e -> {
            layout.getChildren().clear();
            layout.setAlignment(Pos.TOP_LEFT); // visszaigazítás játék UI-ra
            layout.getChildren().addAll(buildGameUI(startButton));
            startGame(timerLabel, startButton);
        });

        return new Scene(layout, 500, 600);
    }

    private List<javafx.scene.Node> buildGameUI(Button startButton) {
        SudokuGenerator generator = new SudokuGenerator();
        int[][] puzzle = generator.generatePuzzle(40); // 40 mező lesz eltávolítva

        GridPane grid = new GridPane();
        TextField[][] cells = new TextField[9][9];

        timerLabel = new Label("00:00:00");
        timerLabel.setStyle("-fx-font-size: 16;");

        Label resultLabel = new Label();

        Button checkButton = new Button("Ellenőrzés");
        Button showScoresButton = new Button("Eredmények");

        // sudoku tábla létrehozása
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-font-size: 16; -fx-alignment: center;");
                int value = puzzle[row][col];
                if (value != 0) {
                    cell.setText(String.valueOf(value));
                    cell.setDisable(true); // ne legyen aktív mező
                    board[row][col] = value;
                }
                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }

        // ellenőrzés gomb esemény
        checkButton.setOnAction(e -> {
            int[][] currentBoard = new int[9][9];
            boolean allFilled = true;

            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    String text = cells[r][c].getText();
                    if (!cells[r][c].isDisabled()) {
                        if (text.isEmpty()) {
                            allFilled = false;
                        }
                        currentBoard[r][c] = text.isEmpty() ? 0 : Integer.parseInt(text);
                    } else {
                        currentBoard[r][c] = Integer.parseInt(cells[r][c].getText());
                    }
                }
            }

            if (!allFilled) {
                resultLabel.setText("Kérlek töltsd ki az összes mezőt!");
                return;
            }

            System.out.println("Ellenőrzött sudoku tábla:");
            printBoard(currentBoard);

            boolean isValid = SudokuValidator.isValidSudoku(currentBoard);

            if (isValid) {
                TextInputDialog dialog = new TextInputDialog("Név");
                dialog.setHeaderText("Gratulálok! Add meg a neved:");
                dialog.setContentText("Név:");
                dialog.showAndWait().ifPresent(name -> {
                    endGame(name);
                    resultLabel.setText("Helyes megoldás!");
                });
            } else {
                resultLabel.setText("Hibás megoldás!");
            }
        });

        // eredménytábla megjelenítése
        showScoresButton.setOnAction(e -> showScorePopup());

        return List.of(timerLabel, grid, checkButton, showScoresButton, resultLabel);
    }

    private void showScorePopup() {
        try {
            List<ScoreManager.Score> scores = scoreManager.loadScores();
            scores.sort(Comparator.comparingInt(s -> timeToSeconds(s.time)));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Math.min(5, scores.size()); i++) {
                ScoreManager.Score score = scores.get(i);
                sb.append((i + 1)).append(". ")
                        .append(score.player).append(" - ")
                        .append(score.time).append(" idő ");
            }

            Stage scoreStage = new Stage();
            scoreStage.setTitle("Eredménytábla");

            TextArea scoreArea = new TextArea();
            scoreArea.setEditable(false);
            scoreArea.setStyle("-fx-font-size: 14; -fx-font-family: 'Consolas';");
            scoreArea.setText(sb.toString().isEmpty() ? "Nincsenek mentett eredmények." : sb.toString());

            VBox scoreLayout = new VBox(10, new Label("Top játékosok:"), scoreArea);
            scoreLayout.setPadding(new Insets(10));

            Scene scoreScene = new Scene(scoreLayout, 300, 250);
            scoreStage.setScene(scoreScene);
            scoreStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // kezdés + stopper indítása
    public void startGame(Label timerLabel, Button startButton) {
        if (!isGameRunning) {
            isGameRunning = true;
            startButton.setDisable(true); // indítás után ne lehessen kattintani

            seconds.set(0);
            minutes.set(0);
            hours.set(0);

            timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer(timerLabel)));
            timerTimeline.setCycleCount(Timeline.INDEFINITE);
            timerTimeline.play();
        }
    }

    private void updateTimer(Label timerLabel) {
        seconds.set(seconds.get() + 1);
        if (seconds.get() == 60) {
            seconds.set(0);
            minutes.set(minutes.get() + 1);
        }
        if (minutes.get() == 60) {
            minutes.set(0);
            hours.set(hours.get() + 1);
        }

        String formattedTime = String.format("%02d:%02d:%02d", hours.get(), minutes.get(), seconds.get());
        timerLabel.setText(formattedTime);
    }

    // játék befejezése, időzítő stop
    public void endGame(String playerName) {
        // idő formátumban fogjuk elmenteni, nem másodpercben
        if (timerTimeline != null) {
            timerTimeline.stop();
        }

        try {
            // idő formátumban mentés
            String formattedTime = String.format("%02d:%02d:%02d", hours.get(), minutes.get(), seconds.get());
            scoreManager.saveScore(playerName, formattedTime);
            System.out.println("Game ended. Score saved for " + playerName + ": " + formattedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}
