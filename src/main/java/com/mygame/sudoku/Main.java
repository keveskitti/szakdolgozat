package com.mygame.sudoku;

import javafx.application.Application;
import javafx.application.Platform;  // Import Platform
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private IntegerProperty seconds = new SimpleIntegerProperty(0);
    private IntegerProperty minutes = new SimpleIntegerProperty(0);
    private IntegerProperty hours = new SimpleIntegerProperty(0);
    private Label timerLabel;  // To display the timer

    @Override
    public void start(Stage primaryStage) {  // fix sudoku teszt céljából
        // Create SudokuGenerator instance to generate a puzzle
        SudokuGenerator generator = new SudokuGenerator();
        int[][] puzzle = generator.generatePuzzle(40);  // Generate puzzle with difficulty 40

        // Create UI components for the game
        GridPane grid = new GridPane();
        TextField[][] cells = new TextField[9][9];

        // Start Game button and Timer label
        Button startButton = new Button("Start Game");
        timerLabel = new Label("00:00:00");  // Initialize the timer label

        startButton.setOnAction(e -> {
            startGame();
            startButton.setDisable(true);  // Disable the button after starting the game
        });

        // Initialize the Sudoku grid
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
                    // Allow only values 1-9
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

        // Layout with the Start button and Timer
        VBox layout = new VBox(10, startButton, timerLabel, grid, checkButton, showScoresButton, resultLabel);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
    }

    // Start the game and timer
    private void startGame() {
        startTime = System.currentTimeMillis();
        Timer timer = new Timer();
        timer.start();
    }

    // Timer class to update the elapsed time
    private class Timer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    updateTimer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateTimer() {
        seconds.set(seconds.get() + 1);
        if (seconds.get() == 60) {
            seconds.set(0);
            minutes.set(minutes.get() + 1);
        }
        if (minutes.get() == 60) {
            minutes.set(0);
            hours.set(hours.get() + 1);
        }

        // Update the timer label with formatted time on the JavaFX application thread
        Platform.runLater(() -> {
            String formattedTime = String.format("%02d:%02d:%02d", hours.get(), minutes.get(), seconds.get());
            timerLabel.setText(formattedTime);
        });
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
