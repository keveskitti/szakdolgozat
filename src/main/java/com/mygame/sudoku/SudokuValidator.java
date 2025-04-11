package com.mygame.sudoku;

public class SudokuValidator {
    public static boolean isValidSudoku(int[][] board) {
        // megnézem ki van-e töltve a tábla
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    return true; // ha van üres akkor a játékos folytathatja
                }
            }
        }

    // ha ki van töltve, ellenőrzöm érvényes-e
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

        return true; // ha minden ell. sikeres akkor érvényes a tábla
}

    private static boolean isValidMove(int[][] board, int row, int col, int num) {
        // beírt szám érvényes-e a sorban, oszlopbn és blokkban is
        return !isInRow(board, row, num) && !isInCol(board, col, num) && !isInSubgrid(board, row, col, num);
    }

    private static boolean isInRow(int[][] board, int row, int num) {
        // ell. sorban
        for (int col = 0; col < 9; col++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInCol(int[][] board, int col, int num) {
        // ell. oszlopban
        for (int row = 0; row < 9; row++) {
            if (board[row][col] == num) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInSubgrid(int[][] board, int row, int col, int num) {
        // 3x3 blokk kezdő sora és oszlopa:
        int subgridRow = row / 3 * 3;
        int subgridCol = col / 3 * 3;

        // ell. a blokkban
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