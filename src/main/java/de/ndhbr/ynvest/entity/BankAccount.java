package de.ndhbr.ynvest.entity;

import de.ndhbr.ynvest.entity.util.SingleIdEntity;
import de.ndhbr.ynvest.util.MathUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import java.util.Random;

@Entity
public class BankAccount extends SingleIdEntity<String> {
    @Id
    @Column(length=64)
    @NotBlank
    private String iban;
    private String username;
    private String password;
    private double balance = 0;
    private double virtualBalance = 0;
    @OneToOne(mappedBy = "bankAccount")
    private Customer customer;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public double getRoundedBalance() {
        return MathUtils.round(getBalance(), 2);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getVirtualBalance() {
        return virtualBalance;
    }

    public double getRoundedVirtualBalance() {
        return MathUtils.round(getVirtualBalance(), 2);
    }

    public void setVirtualBalance(double virtualBalance) {
        this.virtualBalance = virtualBalance;
    }

    public void decreaseVirtualBalance(double amount) {
        if (this.virtualBalance < amount) {
            this.virtualBalance = 0;
        } else {
            this.virtualBalance -= amount;
        }
    }

    public void increaseVirtualBalance(double amount) {
        this.virtualBalance += amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String getID() {
        return iban;
    }
}
