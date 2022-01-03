package de.ndhbr.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Share {
    private String isin;
    private int quantity;

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
