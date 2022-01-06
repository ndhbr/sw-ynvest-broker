package de.ndhbr.ynvest.service;

import de.ndhbr.ynvest.entity.BankAccount;

public interface BankAccountServiceIF {
    BankAccount getBankAccountByIban(String iban);
    BankAccount handleNewBuyOrder(BankAccount bankAccount, double amount);
    BankAccount handleNewSellOrder(BankAccount bankAccount, double amount);
    BankAccount saveBankAccount(BankAccount bankAccount);
}
