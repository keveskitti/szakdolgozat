package com.mygame.sudoku;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.*;

public class SudokuController {
    @FXML
    private GridPane boardGrid;
    private final TextField[][] textFields = new TextField[9][9];

    private final SudokuGame game = new SudokuGame();

    @FXML
    public void handleCheck() {
        System.out.println("Handling check action...");
        readBoardFromUI();

        if (game.isValid()) {
            System.out.println("Board is valid!");
        } else {
            System.out.println("Invalid Sudoku!");
        }
    }

    @FXML
    public void handleReset() {
        System.out.println("Handling reset action...");
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                game.setCell(row, col, 0);
                textFields[row][col].clear();
            }
        }
    }

    @FXML
    public void handleGenerate() {
        System.out.println("Handling generate new puzzle...");
        // Use the game class to generate a new puzzle
        game.generateNewPuzzle();

        // Update the UI with the generated puzzle
        loadBoardToUI();
    }

    @FXML
    public void initialize() {
        System.out.println("SudokuController initialized");
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField tf = new TextField();
                tf.setPrefWidth(40);
                tf.setPrefHeight(40);
                tf.setStyle("-fx-font-size: 16; -fx-alignment: center;");
                boardGrid.add(tf, col, row);
                textFields[row][col] = tf;
            }
        }

        System.out.println("Generating and loading puzzle...");
        handleGenerate();  // Generate and load the puzzle immediately
    }

    @FXML
    public void onSaveClicked() {
        System.out.println("Handling save action...");
        readBoardFromUI();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("sudoku.save"))) {
            out.writeObject(game);
            System.out.println("Game saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLoadClicked() {
        System.out.println("Handling load action...");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("sudoku.save"))) {
            SudokuGame loadedGame = (SudokuGame) in.readObject();
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    game.setCell(row, col, loadedGame.getCell(row, col));
                }
            }
            loadBoardToUI();
            System.out.println("Game loaded.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readBoardFromUI() {
        // Read the current values from the UI into the game model
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = textFields[row][col].getText();
                int val = 0;
                if (!text.isEmpty()) {
                    try {
                        val = Integer.parseInt(text);
                    } catch (NumberFormatException e) {
                        val = 0;
                    }
                }
                game.setCell(row, col, val);
            }
        }
    }

    private void loadBoardToUI() {
        // Update the UI (TextFields) with the current values from the game model
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = game.getCell(row, col);
                textFields[row][col].setText(value == 0 ? "" : String.valueOf(value));
            }
        }
    }
}
