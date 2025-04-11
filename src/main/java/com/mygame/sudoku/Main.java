package com.mygame.sudoku;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

public class Main extends Application {
    private SudokuGame game = new SudokuGame();
    private long startTime;

    @Override
    public void start(Stage primaryStage) {  // fix sudoku teszt céljából
        int[][] puzzle = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        startTime = System.currentTimeMillis();

        GridPane grid = new GridPane();
        TextField[][] cells = new TextField[9][9];

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField cell = new TextField();
                cell.setPrefSize(50, 50);
                cell.setStyle("-fx-font-size: 16; -fx-alignment: center;");

                int value = puzzle[row][col];
                if (value != 0) {
                    cell.setText(String.valueOf(value));
                    cell.setDisable(true); // Make it non-editable
                } else {
                    // input csak 1-9 lehessen
                    cell.textProperty().addListener((obs, oldVal, newVal) -> {
                        if (!newVal.matches("[1-9]?")) {
                            cell.setText(oldVal);
                        }
                    });
                }

                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }

        Label resultLabel = new Label();
        Button checkButton = new Button("Ellenőrzés");
        Button showScoresButton = new Button("Eredmények");

        checkButton.setOnAction(e -> {
            int[][] board = new int[9][9];

            // mezők kitöltésének biztosítása
            boolean allFilled = true;
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    String text = cells[r][c].getText();
                    if (!cells[r][c].isDisabled()) {  // csak a módosítható cellákat ellenőrzöm
                        if (text.isEmpty()) {
                            allFilled = false;
                        }
                        board[r][c] = text.isEmpty() ? 0 : Integer.parseInt(text);  // játékos cellái
                    } else {
                        // fix cellák ne változzanak
                        board[r][c] = Integer.parseInt(cells[r][c].getText());  // megadott cellák
                    }
                }
            }

            if (!allFilled) {
                resultLabel.setText("Kérlek töltsd ki az összes mezőt!");
                return;
            }

            // debug: aktuális sudoku tábla
            System.out.println("Ellenőrzött sudoku tábla:");
            printBoard(board);

            // játékos input ellenőrzése
            boolean isValid = SudokuValidator.isValidSudoku(board);

            // eredmény kiírása
            if (isValid) {
                long endTime = System.currentTimeMillis();
                int timeTakenSeconds = (int) ((endTime - startTime) / 1000);

                TextInputDialog dialog = new TextInputDialog("Név");
                dialog.setHeaderText("Gratulálok! Add meg a neved:");
                dialog.setContentText("Név:");
                dialog.showAndWait().ifPresent(name -> {
                    ScoreManager sm = new ScoreManager();
                    try {
                        sm.saveScore(name, timeTakenSeconds);
                        resultLabel.setText("Helyes megoldás! Idő: " + timeTakenSeconds + " másodperc.");
                    } catch (Exception ex) {
                        resultLabel.setText("Nem sikerült elmenteni az eredményt.");
                        ex.printStackTrace();
                    }
                });
            } else {
                resultLabel.setText("Hibás megoldás!");
            }
        });

        // eredménytábla megjelenítése új ablakban
        showScoresButton.setOnAction(e -> {
            ScoreManager sm = new ScoreManager();
            try {
                List<ScoreManager.Score> scores = sm.loadScores();
                scores.sort(Comparator.comparingInt(s -> s.time));

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(5, scores.size()); i++) {
                    ScoreManager.Score score = scores.get(i);
                    sb.append((i + 1)).append(". ")
                            .append(score.player).append(" - ")
                            .append(score.time).append(" másodperc\n");
                }

                Stage scoreStage = new Stage();
                scoreStage.setTitle("Eredménytábla");

                TextArea scoreArea = new TextArea();
                scoreArea.setEditable(false);
                scoreArea.setStyle("-fx-font-size: 14; -fx-font-family: 'Consolas';");

                String scoreText = sb.toString().isEmpty()
                        ? "Nincsenek mentett eredmények."
                        : sb.toString();
                scoreArea.setText(scoreText);

                VBox scoreLayout = new VBox(10, new Label("Top játékosok:"), scoreArea);
                scoreLayout.setPadding(new Insets(10));

                Scene scoreScene = new Scene(scoreLayout, 300, 250);
                scoreStage.setScene(scoreScene);
                scoreStage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(10, grid, checkButton, showScoresButton, resultLabel);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
