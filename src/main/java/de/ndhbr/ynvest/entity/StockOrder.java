package de.ndhbr.ynvest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import de.othr.sw.yetra.dto.OrderDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

import static de.ndhbr.ynvest.util.MathUtils.round;

@Entity
public class StockOrder {
    @Id
    @JsonProperty("orderId")
    private long orderId;
    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderType type;
    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;
    @NotBlank
    private String isin;
    @NotNull
    private int quantity;
    @NotNull
    private double unitPrice;
    @ManyToOne
    private Customer customer;
    @NotNull
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
        return round(getUnitPrice() * getQuantity(), 2);
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

    public void mergeWith(OrderDTO orderDTO) {
        Instant instant = orderDTO.getTimestamp().toInstant(ZoneOffset.UTC);

        setOrderId(orderDTO.getId());
        setPlacedOn(Date.from(instant));
        setUnitPrice(orderDTO.getUnitPrice());

        if (orderDTO.getStatus() == de.othr.sw.yetra.entity.OrderStatus.OPEN) {
            setStatus(de.ndhbr.ynvest.entity.OrderStatus.Open);
        } else {
            setStatus(de.ndhbr.ynvest.entity.OrderStatus.Completed);
        }
    }
}
