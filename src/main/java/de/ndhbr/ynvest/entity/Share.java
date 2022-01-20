package de.ndhbr.ynvest.entity;

import org.hibernate.service.spi.ServiceException;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Embeddable
public class Share {
    @NotBlank
    private String isin;
    @NotNull
    private int quantity;
    @NotNull
    private double purchasePrice;

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

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) throws ServiceException {
        if (this.quantity - quantity < 0) {
            throw new ServiceException("Nicht genügend Anteile");
        }

        this.quantity -= quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void calculateNewPurchasePrice(double newPurchasePrice, int quantity) {
        this.purchasePrice = ((this.purchasePrice * this.quantity) +
                newPurchasePrice * quantity) / (this.quantity + quantity);
    }
}
