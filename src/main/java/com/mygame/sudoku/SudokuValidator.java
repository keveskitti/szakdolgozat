package com.mygame.sudoku;

public class SudokuValidator {
    public static boolean isValidSudoku(int[][] board) {
        // ell. van-.e üres cella
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    return true;
                }
            }
        }
        return isValidBoard(board);
    }

    private static boolean isValidBoard(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int num = board[r][c];
                if (num != 0) {
                    board[r][c] = 0;
                    if (!isValidMove(board, r, c, num)) {
                        board[r][c] = num;
                        return false;
                    }
                    board[r][c] = num;
                }
            }
        }
        return true;
    }

    private static boolean isValidMove(int[][] board, int row, int col, int num) {
        return !isInRow(board, row, num) && !isInCol(board, col, num) && !isInSubgrid(board, row, col, num);
    }

    private static boolean isInRow(int[][] board, int row, int num) {
        // szám ell. sorban
        for (int col = 0; col < 9; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInCol(int[][] board, int col, int num) {
        // szám ell. oszlopban
        for (int row = 0; row < 9; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInSubgrid(int[][] board, int row, int col, int num) {
        int subgridRow = row / 3 * 3;
        int subgridCol = col / 3 * 3;

        // szám ell. 3x3-ban
        for (int i = subgridRow; i < subgridRow + 3; i++) {
            for (int j = subgridCol; j < subgridCol + 3; j++) {
                if (board[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }
}
