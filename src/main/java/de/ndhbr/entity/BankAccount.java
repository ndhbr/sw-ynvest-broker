package de.ndhbr.entity;

import de.ndhbr.entity.util.SingleIdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BankAccount extends SingleIdEntity<String> {
    @Id
    @Column(length=64)
    private String iban;
    private String username;
    private String password;
    private String eventUrl;
    private double balance;
    private double virtualBalance;

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String nutzername) {
        this.username = nutzername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String passwort) {
        this.password = passwort;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String ereignisUrl) {
        this.eventUrl = ereignisUrl;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double kontostand) {
        this.balance = kontostand;
    }

    public double getVirtualBalance() {
        return virtualBalance;
    }

    public void setVirtualBalance(double virtuellerKontostand) {
        this.virtualBalance = virtuellerKontostand;
    }

    @Override
    public String getID() {
        return iban;
    }
}
