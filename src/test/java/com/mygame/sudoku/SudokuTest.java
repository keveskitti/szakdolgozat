package com.mygame.sudoku;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Random;

public class SudokuTest {

    @Test
    public void testRandomizedBoards() {
        for (int i = 0; i < 10; i++) {
            int[][] randomizedBoard = generateValidRandomizedBoard();
            System.out.println("Tábla #" + (i + 1) + " tesztelése...");
            printBoard(randomizedBoard);

            boolean isValid = SudokuValidator.isValidSudoku(randomizedBoard);

            // Print the validity message
            System.out.println("Tábla #" + (i + 1) + (isValid ? " helyes!\n" : " helytelen!\n"));

        }
    }

    private void printBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    private int[][] generateValidRandomizedBoard() {
        int[][] board = new int[9][9];
        Random rand = new Random();

        // Fill the board randomly while ensuring the validity of the board
        if (generateRandomBoardWithBacktracking(board, rand)) {
            return board;
        }

        // Return an empty board if unable to generate a valid board (shouldn't happen often)
        return new int[9][9];
    }

    // Backtracking algorithm to generate a random and valid Sudoku board
    private boolean generateRandomBoardWithBacktracking(int[][] board, Random rand) {
        // Try to fill each cell randomly using backtracking
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) { // Find the next empty cell
                    // Shuffle the numbers 1-9 to randomize the order
                    int[] shuffledNumbers = generateShuffledNumbers(rand);
                    for (int num : shuffledNumbers) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num; // Place number
                            if (generateRandomBoardWithBacktracking(board, rand)) {
                                return true;
                            }
                            board[row][col] = 0; // Backtrack
                        }
                    }
                    return false; // No valid number could be placed here
                }
            }
        }
        return true; // All cells are filled correctly
    }

    // Helper method to validate if placing a number in a given cell is valid
    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check the row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;  // Number already exists in the row
            }
        }
        // Check the column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;  // Number already exists in the column
            }
        }
        // Check the 3x3 grid
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;  // Number already exists in the 3x3 block
                }
            }
        }
        return true;  // It's valid to place num in the cell
    }

    // Helper method to generate a random shuffled array of numbers 1-9
    private int[] generateShuffledNumbers(Random rand) {
        int[] numbers = new int[9];
        for (int i = 0; i < 9; i++) {
            numbers[i] = i + 1;
        }

        // Shuffle the numbers array to randomize it
        for (int i = 0; i < 9; i++) {
            int randomIndex = rand.nextInt(9);
            // Swap the current number with the randomly chosen number
            int temp = numbers[i];
            numbers[i] = numbers[randomIndex];
            numbers[randomIndex] = temp;
        }
        return numbers;
    }
}
