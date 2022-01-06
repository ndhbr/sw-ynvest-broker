package de.ndhbr.ynvest.entity;

import de.ndhbr.ynvest.entity.util.SingleIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class BankAccount extends SingleIdEntity<String> {
    @Id
    @Column(length=64)
    private String iban;
    private String username;
    private String password;
    private String eventUrl;
    private double balance = 0;
    private double virtualBalance = 0;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setRandomIbanTmp() {
        // TODO: REMOVE, this is only before connecting to Bank Amann
        Random rand = new Random();
        StringBuilder card = new StringBuilder("DE");
        for (int i = 0; i < 14; i++)
        {
            int n = rand.nextInt(10);
            card.append(Integer.toString(n));
        }

        this.setIban(card.toString());
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

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getVirtualBalance() {
        return virtualBalance;
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

    @Override
    public String getID() {
        return iban;
    }
}
