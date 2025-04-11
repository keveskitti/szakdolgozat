package com.mygame.sudoku;

public class SudokuBoard {
    private int[][] board;

    public SudokuBoard() {
    }

    public SudokuBoard(int[][] board) {
        this.board = board;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}
