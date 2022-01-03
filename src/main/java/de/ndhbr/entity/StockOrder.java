package de.ndhbr.entity;

import javax.persistence.*;
import java.util.Date;

import static de.ndhbr.util.MathUtils.round;

@Entity
public class StockOrder {
    @Id
    private long orderId;
    @Enumerated(EnumType.STRING)
    private OrderType type;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String isin;
    private int quantity;
    private double unitPrice;
    private String eventUrl;
    private Date placedOn;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long auftragsNr) {
        this.orderId = auftragsNr;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType typ) {
        this.type = typ;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int menge) {
        this.quantity = menge;
    }

    public double getUnitPrice() {
        return round(unitPrice, 2);
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public Date getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(Date placedOn) {
        this.placedOn = placedOn;
    }
}
