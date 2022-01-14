package de.ndhbr.ynvest.entity;

public enum OrderType {
    Buy("Kauf"),
    Sell("Verkauf");

    private final String name;

    OrderType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
