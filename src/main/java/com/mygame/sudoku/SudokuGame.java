package com.mygame.sudoku;

import java.io.Serializable;

public class SudokuGame implements Serializable {
    private int[][] board;

    // Constructor that initializes the Sudoku board with 0s (empty cells).
    public SudokuGame() {
        board = new int[9][9]; // Create a 9x9 Sudoku board
        generateNewPuzzle(); // Populate the board with a new puzzle on game start
    }

    // Public method to generate a new Sudoku puzzle and set it on the board
    public void generateNewPuzzle() {
        // For simplicity, we're using a pre-generated solved puzzle for now
        int[][] solvedBoard = SudokuGenerator.generateSolvedSudoku(); // This method generates a solved Sudoku
        int[][] puzzleBoard = SudokuGenerator.generatePuzzle(solvedBoard, 40); // Create a puzzle with 40 clues

        // Set the generated puzzle on the board
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                board[row][col] = puzzleBoard[row][col]; // Set the puzzle board on the actual board
            }
        }
    }

    // Method to set the value of a specific cell in the board
    public void setCell(int row, int col, int value) {
        board[row][col] = value; // Set the specified cell's value
    }

    // Method to get the value of a specific cell in the board
    public int getCell(int row, int col) {
        return board[row][col]; // Return the value of the specified cell
    }

    // Method to check if the Sudoku board is valid (rows, columns, and 3x3 subgrids)
    public boolean isValid() {
        return checkRows() && checkCols() && checkBoxes();
    }

    // Method to check if all rows are valid (no duplicate values)
    private boolean checkRows() {
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10]; // Tracks numbers from 1 to 9
            for (int col = 0; col < 9; col++) {
                int num = board[row][col];
                if (num != 0 && seen[num]) return false; // Duplicate found
                seen[num] = true;
            }
        }
        return true;
    }

    // Method to check if all columns are valid (no duplicate values)
    private boolean checkCols() {
        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10]; // Tracks numbers from 1 to 9
            for (int row = 0; row < 9; row++) {
                int num = board[row][col];
                if (num != 0 && seen[num]) return false; // Duplicate found
                seen[num] = true;
            }
        }
        return true;
    }

    // Method to check if all 3x3 subgrids are valid (no duplicate values)
    private boolean checkBoxes() {
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                boolean[] seen = new boolean[10]; // Tracks numbers from 1 to 9
                // Check all cells in the 3x3 block
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int row = blockRow * 3 + i;  // Row of the current cell in the block
                        int col = blockCol * 3 + j;  // Column of the current cell in the block
                        int num = board[row][col];   // The number in the current cell

                        if (num != 0) {
                            if (seen[num]) return false; // Duplicate found in the 3x3 block
                            seen[num] = true;
                        }
                    }
                }
            }
        }
        return true;
    }
}
