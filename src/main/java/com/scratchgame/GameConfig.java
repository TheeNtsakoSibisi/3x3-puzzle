package com.scratchgame;// File: GameConfig.java

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import com.scratchgame.*;

public class GameConfig {
    public int rows;
    public int columns;
    public Map<String, Symbol> symbols = new HashMap<>();
    public List<ProbabilityEntry> standardProbabilities = new ArrayList<>();
    public Map<String, Integer> bonusProbabilities = new HashMap<>();
    public Map<String, WinCombination> winCombinations = new HashMap<>();

    public static GameConfig fromJson(JsonNode json) {
        GameConfig config = new GameConfig();

        config.columns = json.has("columns") ? json.get("columns").asInt() : 3;
        config.rows = json.has("rows") ? json.get("rows").asInt() : 3;

        // Parse symbols
        JsonNode symbolsNode = json.get("symbols");
        symbolsNode.fields().forEachRemaining(entry -> {
            String symbolName = entry.getKey();
            JsonNode symbolProps = entry.getValue();
            String type = symbolProps.get("type").asText();

            if (type.equals("standard")) {
                double rewardMultiplier = symbolProps.get("reward_multiplier").asDouble();
                config.symbols.put(symbolName, new StandardSymbol(symbolName, rewardMultiplier));
            } else {
                String impact = symbolProps.get("impact").asText();
                double rewardMultiplier = symbolProps.has("reward_multiplier") ? symbolProps.get("reward_multiplier").asDouble() : 0;
                double extra = symbolProps.has("extra") ? symbolProps.get("extra").asDouble() : 0;
                config.symbols.put(symbolName, new BonusSymbol(symbolName, impact, rewardMultiplier, extra));
            }
        });

        // Parse standard symbol probabilities
        JsonNode standardNode = json.get("probabilities").get("standard_symbols");
        for (JsonNode probNode : standardNode) {
            int col = probNode.get("column").asInt();
            int row = probNode.get("row").asInt();

            Map<String, Integer> probs = new HashMap<>();
            probNode.get("symbols").fields().forEachRemaining(e -> {
                probs.put(e.getKey(), e.getValue().asInt());
            });

            config.standardProbabilities.add(new ProbabilityEntry(row, col, probs));
        }

        // Parse bonus probabilities
        JsonNode bonusNode = json.get("probabilities").get("bonus_symbols").get("symbols");
        bonusNode.fields().forEachRemaining(e -> {
            config.bonusProbabilities.put(e.getKey(), e.getValue().asInt());
        });

        // Parse win combinations
        JsonNode winCombNode = json.get("win_combinations");
        winCombNode.fields().forEachRemaining(e -> {
            config.winCombinations.put(e.getKey(), WinCombination.fromJson(e.getValue()));
        });

        return config;
    }
}
