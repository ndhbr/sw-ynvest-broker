package de.ndhbr.ynvest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.ndhbr.ynvest.util.MathUtils;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static de.ndhbr.ynvest.util.MathUtils.round;

@Entity
public class StockOrder {
    @Id
    @JsonProperty("orderId")
    private long orderId;
    @Enumerated(EnumType.STRING)
    private OrderType type;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String isin;
    private int quantity;
    private double unitPrice;
    private String eventUrl;
    @ManyToOne
    private Customer customer;
    private Date placedOn;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return round(unitPrice, 2);
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getRoundedTotalPrice() {
        return MathUtils.round(getUnitPrice() * getQuantity(), 2);
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getPlacedOn() {
        return placedOn;
    }

    public String getPlacedOnHumanReadable() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(placedOn) + " Uhr";
    }

    public void setPlacedOn(Date placedOn) {
        this.placedOn = placedOn;
    }
}
