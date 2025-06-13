package com.scratchgame;
import java.util.Map;

public class ProbabilityEntry {
    public int row;
    public int column;
    public Map<String, Integer> symbolProbabilities;

    public ProbabilityEntry(int row, int column, Map<String, Integer> symbolProbabilities) {
        this.row = row;
        this.column = column;
        this.symbolProbabilities = symbolProbabilities;
    }
}
