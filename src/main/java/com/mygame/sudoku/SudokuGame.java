package com.mygame.sudoku;

public class SudokuGame {
    private int[][] board;

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
}