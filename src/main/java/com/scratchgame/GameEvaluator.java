package com.scratchgame;// File: GameEvaluator.java

import java.util.*;

public class GameEvaluator {
    private GameConfig config;

    public GameEvaluator(GameConfig config) {
        this.config = config;
    }

    public GameResult evaluate(String[][] matrix, double betAmount) {
        Map<String, List<String>> appliedWinningCombinations = new HashMap<>();
        Map<String, Double> symbolRewards = new HashMap<>();
        Set<String> winningSymbols = new HashSet<>();

        // Count occurrences
        Map<String, Integer> occurrences = new HashMap<>();
        for (String[] row : matrix) {
            for (String cell : row) {
                Symbol symbol = config.symbols.get(cell);
                if (symbol instanceof StandardSymbol) {
                    occurrences.put(cell, occurrences.getOrDefault(cell, 0) + 1);
                }
            }
        }

        // Check same_symbols conditions
        for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();
            double reward = betAmount * ((StandardSymbol) config.symbols.get(symbol)).rewardMultiplier;
            List<String> matchedCombs = new ArrayList<>();
            for (Map.Entry<String, WinCombination> combEntry : config.winCombinations.entrySet()) {
                WinCombination comb = combEntry.getValue();
                if ("same_symbols".equals(comb.when) && count >= comb.count) {
                    reward *= comb.rewardMultiplier;
                    matchedCombs.add(combEntry.getKey());
                }
            }
            if (!matchedCombs.isEmpty()) {
                appliedWinningCombinations.put(symbol, matchedCombs);
                symbolRewards.put(symbol, reward);
                winningSymbols.add(symbol);
            }
        }

        double totalReward = symbolRewards.values().stream().mapToDouble(Double::doubleValue).sum();

        // Apply bonus if any
        String appliedBonus = null;
        if (totalReward > 0) {
            appliedBonus = applyBonus(matrix, totalReward);
            if (appliedBonus != null) {
                Symbol bonusSymbol = config.symbols.get(appliedBonus);
                if (bonusSymbol instanceof BonusSymbol bonus) {
                    switch (bonus.impact) {
                        case "multiply_reward" -> totalReward *= bonus.rewardMultiplier;
                        case "extra_bonus" -> totalReward += bonus.extra;
                    }
                }
            }
        }

        return new GameResult(matrix, totalReward, appliedWinningCombinations, appliedBonus);
    }

    private String applyBonus(String[][] matrix, double rewardBeforeBonus) {
        for (String[] row : matrix) {
            for (String cell : row) {
                Symbol symbol = config.symbols.get(cell);
                if (symbol instanceof BonusSymbol) {
                    return cell;
                }
            }
        }
        return null;
    }
}
