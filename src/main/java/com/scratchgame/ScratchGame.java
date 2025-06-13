package com.scratchgame;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


import java.io.File;

public class ScratchGame {

    public static void main(String[] args) throws Exception {
        if (args.length < 4 || !args[0].equals("--config") || !args[2].equals("--betting-amount")) {
            System.out.println("Usage: java -jar scratchgame.jar --config config.json --betting-amount 100");
            return;
        }

        String configPath = args[1];
        double bettingAmount = Double.parseDouble(args[3]);

        // Load configuration
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode configJson = objectMapper.readTree(new File(configPath));

        GameConfig config = GameConfig.fromJson(configJson);

        // Generate the matrix
        GameMatrixGenerator matrixGenerator = new GameMatrixGenerator(config);
        String[][] matrix = matrixGenerator.generateMatrix();

        // Evaluate winnings
        GameEvaluator evaluator = new GameEvaluator(config);
        GameResult result = evaluator.evaluate(matrix, bettingAmount);

        // Output
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(System.out, result);
    }
}
