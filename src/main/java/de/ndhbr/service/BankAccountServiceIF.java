package de.ndhbr.service;

import de.ndhbr.entity.BankAccount;

public interface BankAccountServiceIF {
    BankAccount getBankAccountByIban(String iban);
    BankAccount handleNewBuyOrder(BankAccount bankAccount, double amount);
    BankAccount handleNewSellOrder(BankAccount bankAccount, double amount);
    BankAccount saveBankAccount(BankAccount bankAccount);
}
