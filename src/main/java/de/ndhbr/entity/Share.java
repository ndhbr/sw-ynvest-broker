package de.ndhbr.entity;

import org.hibernate.service.spi.ServiceException;

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

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseQuantity(int quantity) throws ServiceException {
        if (this.quantity - quantity < 0) {
            throw new ServiceException("Nicht genügend Anteile");
        }

        this.quantity -= quantity;
    }
}
