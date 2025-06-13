package com.scratchgame;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;

public class WinCombination {
    public String when;
    public String group;
    public double rewardMultiplier;
    public int count;
    public List<List<String>> coveredAreas = new ArrayList<>();

    public static WinCombination fromJson(JsonNode json) {
        WinCombination comb = new WinCombination();
        comb.when = json.get("when").asText();
        comb.group = json.get("group").asText();
        comb.rewardMultiplier = json.get("reward_multiplier").asDouble();
        if (json.has("count")) {
            comb.count = json.get("count").asInt();
        }
        if (json.has("covered_areas")) {
            for (JsonNode area : json.get("covered_areas")) {
                List<String> posList = new ArrayList<>();
                for (JsonNode pos : area) {
                    posList.add(pos.asText());
                }
                comb.coveredAreas.add(posList);
            }
        }
        return comb;
    }
}
