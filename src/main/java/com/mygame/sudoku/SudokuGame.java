package com.mygame.sudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SudokuGame {
    private int[][] board;
    private IntegerProperty seconds = new SimpleIntegerProperty(0);  // Time in seconds
    private IntegerProperty minutes = new SimpleIntegerProperty(0);  // Time in minutes
    private IntegerProperty hours = new SimpleIntegerProperty(0);    // Time in hours
    private boolean isGameRunning = false;
    private Timeline timerTimeline;
    private ScoreManager scoreManager = new ScoreManager();  // For saving scores

    public SudokuGame() {
        board = new int[9][9]; // sudoku tábla létrehozása
    }

    public void setCell(int row, int col, int value) {
        board[row][col] = value; // beállítja a megadott cella értékét
    }

    public int getCell(int row, int col) {
        return board[row][col]; // visszaadja a megadott cella értékét
    }

    public boolean isValid() {
        // táblázat érvényes-e
        return checkRows() && checkCols() && checkBoxes();
    }

    private boolean checkRows() {
        // sorok ell.
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int num = board[row][col]; // megnézem mi a sor akt. cellájában a szám
                if (num != 0 && seen[num]) return false; // ha már van ilyen akkor hibás
                seen[num] = true;
            }
        }
        return true;
    }

    private boolean checkCols() {
        // oszlopok ell.
        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int num = board[row][col]; // innen ugyanaz mint a sornál csak oszlopra
                if (num != 0 && seen[num]) return false;
                seen[num] = true;
            }
        }
        return true;
    }

    private boolean checkBoxes() {
        // 3x3 blokkok ell.
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                boolean[] seen = new boolean[10];
                // minden cellát megnézek a blokkban
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = blockRow * 3 + i;  // blokk sora
                        int col = blockCol * 3 + j;  // blokk oszlopa
                        int num = board[row][col];   // aktuális cella száma

                        if (num != 0) {
                            if (seen[num]) return false; // ha már van ilyen akkor hibás
                            seen[num] = true;
                        }
                    }
                }
            }
        }
        return true;
    }

    // kezdés + stopper indítása
    public void startGame(Text timerText, Button startButton) {
        if (!isGameRunning) {
            isGameRunning = true;
            startButton.setDisable(true); // Disable start button once game starts

            // időzítő visszaállítása
            seconds.set(0);
            minutes.set(0);
            hours.set(0);

            timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
            timerTimeline.setCycleCount(Timeline.INDEFINITE);
            timerTimeline.play();
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
    }

    // játék befejezése, időzítő stop
    public void endGame(String playerName) {
        if (timerTimeline != null) {
            timerTimeline.stop();
        }

        // teljes idő mp-ekben
        int totalTimeInSeconds = hours.get() * 3600 + minutes.get() * 60 + seconds.get();

        // eredmény mentése
        try {
            scoreManager.saveScore(playerName, totalTimeInSeconds);
            System.out.println("Game ended. Score saved for " + playerName + ": " + totalTimeInSeconds + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // rangsor
        try {
            System.out.println("Leaderboard:");
            for (ScoreManager.Score score : scoreManager.loadScores()) {
                System.out.println(score.player + ": " + score.time + " seconds");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
