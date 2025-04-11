package com.mygame.sudoku;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    private static final String FILE = "scores.json";
    private ObjectMapper mapper = new ObjectMapper();

    public void saveScore(String player, int timeInSeconds) throws Exception {
        List<Score> scores = loadScores();
        scores.add(new Score(player, timeInSeconds));
        mapper.writeValue(new File(FILE), scores);
    }

    public List<Score> loadScores() throws Exception {
        File file = new File(FILE);
        if (!file.exists()) return new ArrayList<>();
        return List.of(mapper.readValue(file, Score[].class));
    }

    static class Score {
        public String player;
        public int time;

        public Score() {}
        public Score(String player, int time) {
            this.player = player;
            this.time = time;
        }
    }
}
