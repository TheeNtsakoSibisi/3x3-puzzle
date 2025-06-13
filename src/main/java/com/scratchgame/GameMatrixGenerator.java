package com.scratchgame;// File: GameMatrixGenerator.java

import java.util.*;

public class GameMatrixGenerator {
    private GameConfig config;
    private Random random = new Random();

    public GameMatrixGenerator(GameConfig config) {
        this.config = config;
    }

    public String[][] generateMatrix() {
        String[][] matrix = new String[config.rows][config.columns];

        // generate standard symbols
        for (int i = 0; i < config.rows; i++) {
            for (int j = 0; j < config.columns; j++) {
                String symbol = getRandomStandardSymbol(i, j);
                matrix[i][j] = symbol;
            }
        }

        // randomly replace some cells with bonus symbols
        int totalBonusWeight = config.bonusProbabilities.values().stream().mapToInt(x -> x).sum();
        for (int i = 0; i < config.rows; i++) {
            for (int j = 0; j < config.columns; j++) {
                if (random.nextDouble() < 0.2) {  // 20% chance to have bonus symbol
                    String bonusSymbol = getRandomSymbol(config.bonusProbabilities, totalBonusWeight);
                    matrix[i][j] = bonusSymbol;
                }
            }
        }

        return matrix;
    }

    private String getRandomStandardSymbol(int row, int column) {
        Map<String, Integer> probs = getCellProbability(row, column);
        int totalWeight = probs.values().stream().mapToInt(x -> x).sum();
        return getRandomSymbol(probs, totalWeight);
    }

    private Map<String, Integer> getCellProbability(int row, int column) {
        for (ProbabilityEntry e : config.standardProbabilities) {
            if (e.row == row && e.column == column) return e.symbolProbabilities;
        }
        return config.standardProbabilities.get(0).symbolProbabilities;
    }

    private String getRandomSymbol(Map<String, Integer> probabilities, int totalWeight) {
        int rand = random.nextInt(totalWeight) + 1;
        int cumulative = 0;
        for (Map.Entry<String, Integer> entry : probabilities.entrySet()) {
            cumulative += entry.getValue();
            if (rand <= cumulative) return entry.getKey();
        }
        return probabilities.keySet().iterator().next();
    }
}
