package com.mygame.sudoku;

import java.util.*;

public class SudokuGenerator {
    private static final Random random = new Random();

    // Generate a solved Sudoku board
    public static int[][] generateSolvedSudoku() {
        int[][] board = new int[9][9];

        if (fillBoard(board)) {
            return board;
        } else {
            throw new RuntimeException("Failed to generate a solved Sudoku board.");
        }
    }

    // Fill the Sudoku board recursively
    private static boolean fillBoard(int[][] board) {
        List<int[]> emptyCells = getEmptyCells(board);
        if (emptyCells.isEmpty()) {
            return true;  // Board is fully filled
        }

        // Pick a random empty cell to fill
        int[] cell = emptyCells.get(random.nextInt(emptyCells.size()));
        int row = cell[0];
        int col = cell[1];

        List<Integer> numbers = getShuffledNumbers();
        for (int num : numbers) {
            if (isValid(board, row, col, num)) {
                board[row][col] = num;

                // Recursively fill the board
                if (fillBoard(board)) {
                    return true;
                }

                // Backtrack
                board[row][col] = 0;
            }
        }
        return false;  // No valid number could be placed, so we backtrack
    }

    // Get a shuffled list of numbers from 1 to 9
    private static List<Integer> getShuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    // Get the list of empty cells (cells with value 0)
    private static List<int[]> getEmptyCells(int[][] board) {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }
        return emptyCells;
    }

    // Check if a number can be placed at the given row and column
    private static boolean isValid(int[][] board, int row, int col, int num) {
        return !isInRow(board, row, num) && !isInCol(board, col, num) && !isInSubgrid(board, row, col, num);
    }

    // Check if the number is in the same row
    private static boolean isInRow(int[][] board, int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    // Check if the number is in the same column
    private static boolean isInCol(int[][] board, int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    // Check if the number is in the same 3x3 subgrid
    private static boolean isInSubgrid(int[][] board, int row, int col, int num) {
        int startRow = row / 3 * 3;
        int startCol = col / 3 * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (board[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    // Create a puzzle by removing some values from the solved board
    public static int[][] generatePuzzle(int[][] solvedBoard, int numberOfCellsToRemove) {
        int[][] puzzle = new int[9][9];
        copyBoard(solvedBoard, puzzle);

        int cellsRemoved = 0;
        while (cellsRemoved < numberOfCellsToRemove) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            if (puzzle[row][col] != 0) {
                puzzle[row][col] = 0;
                cellsRemoved++;
            }
        }
        return puzzle;
    }

    // Copy the solved board to a new board
    private static void copyBoard(int[][] source, int[][] destination) {
        for (int i = 0; i < 9; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, 9);
        }
    }
}
