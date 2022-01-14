package de.ndhbr.ynvest.entity;

import com.sun.istack.NotNull;
import org.hibernate.service.spi.ServiceException;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Share {
    @NotBlank
    private String isin;
    @NotNull
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
