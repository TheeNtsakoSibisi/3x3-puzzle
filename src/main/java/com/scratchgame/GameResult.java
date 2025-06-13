package com.scratchgame;
import java.util.Map;

public class GameResult {
    public String[][] matrix;
    public double reward;
    public Map<String, java.util.List<String>> applied_winning_combinations;
    public String applied_bonus_symbol;

    public GameResult(String[][] matrix, double reward, Map<String, java.util.List<String>> appliedCombs, String bonus) {
        this.matrix = matrix;
        this.reward = reward;
        this.applied_winning_combinations = appliedCombs;
        this.applied_bonus_symbol = bonus;
    }
}