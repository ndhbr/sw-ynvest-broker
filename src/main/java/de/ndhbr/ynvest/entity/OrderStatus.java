package de.ndhbr.ynvest.entity;

public enum OrderStatus {
    Open("Offen"),
    Completed("Abgeschlossen");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
