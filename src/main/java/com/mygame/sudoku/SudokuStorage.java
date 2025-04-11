package com.mygame.sudoku;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class SudokuStorage {

    private static final String FILE_PATH = "sudoku_save.json";

    public static void saveBoard(int[][] board) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(FILE_PATH), new SudokuBoard(board));
            System.out.println("Tábla elmentve.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] loadBoard() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            SudokuBoard loaded = mapper.readValue(new File(FILE_PATH), SudokuBoard.class);
            System.out.println("Tábla betöltve.");
            return loaded.getBoard();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
