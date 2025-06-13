package com.scratchgame;// File: Symbol.java

public abstract class Symbol {
    public String name;

    public Symbol(String name) {
        this.name = name;
    }
}

class StandardSymbol extends Symbol {
    public double rewardMultiplier;

    public StandardSymbol(String name, double rewardMultiplier) {
        super(name);
        this.rewardMultiplier = rewardMultiplier;
    }
}

class BonusSymbol extends Symbol {
    public String impact;
    public double rewardMultiplier;
    public double extra;

    public BonusSymbol(String name, String impact, double rewardMultiplier, double extra) {
        super(name);
        this.impact = impact;
        this.rewardMultiplier = rewardMultiplier;
        this.extra = extra;
    }
}
