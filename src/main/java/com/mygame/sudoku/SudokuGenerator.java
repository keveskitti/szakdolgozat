// kitöltött sudoku táblák generálása, véletlenszerűen törölt cellákkal

package com.mygame.sudoku;

import java.util.Random;

public class SudokuGenerator {

    // üres tábla generálása
    private int[][] board = new int[9][9];

    // Ell. biztonságos-e abba a cellába számot írni
    private boolean isSafe(int[][] board, int row, int col, int num) {
        // sorban
        for (int c = 0; c < 9; c++) {
            if (board[row][c] == num) {
                return false;
            }
        }

        // oszlopban
        for (int r = 0; r < 9; r++) {
            if (board[r][col] == num) {
                return false;
            }
        }

        // 3x3-ban
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    // backtracking megoldás
    private boolean solve(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {  // üres cella
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (solve(board)) {
                                return true;  // kövi üres cella ell.
                            }
                            board[row][col] = 0;  // Backtrack
                        }
                    }
                    return false;  // ha nincs helyes szám
                }
            }
        }
        return true;
    }

    // Randomizált számok generálása
    private void randomizeBoard() {
        Random rand = new Random();

        // Az alap Sudoku táblát teljesen véletlenszerűen töltsük fel
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = 0;
            }
        }

        // átlós 3x3 blokkok random feltöltése
        for (int i = 0; i < 9; i += 3) {
            fillDiagonalBlock(i, i);
        }

        // megoldás backtrackinggel
        solve(board);
    }

    // 3x3 blokkok feltöltése véletlenszerűen
    private void fillDiagonalBlock(int row, int col) {
        Random rand = new Random();
        boolean[] used = new boolean[10];

        for (int i = row; i < row + 3; i++) {
            for (int j = col; j < col + 3; j++) {
                int num;
                do {
                    num = rand.nextInt(9) + 1;
                } while (used[num]);
                board[i][j] = num;
                used[num] = true;
            }
        }
    }

    // tábla generálása
    public int[][] generateSolvedBoard() {
        randomizeBoard();
        return board;
    }

    // random cellák törlése
    public int[][] generatePuzzle(int difficulty) {
        int[][] solvedBoard = generateSolvedBoard();

        Random rand = new Random();
        int numberOfCellsToRemove = difficulty;  // törölt cellák számától függő nehézség

        while (numberOfCellsToRemove > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            // csak azt törlöm amelyik nem üres
            if (solvedBoard[row][col] != 0) {
                solvedBoard[row][col] = 0;
                numberOfCellsToRemove--;
            }
        }

        return solvedBoard;
    }

    public void printBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SudokuGenerator generator = new SudokuGenerator();

        int[][] solvedBoard = generator.generateSolvedBoard();
        System.out.println("Megoldott sudoku:");
        generator.printBoard(solvedBoard);

        int[][] puzzle = generator.generatePuzzle(40);
        System.out.println("\nGenerált sudoku tábla (üres cellákkal):");
        generator.printBoard(puzzle);
    }
}
