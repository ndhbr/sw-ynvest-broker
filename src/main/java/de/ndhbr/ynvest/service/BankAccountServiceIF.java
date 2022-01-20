package de.ndhbr.ynvest.service;

import de.ndhbr.ynvest.entity.BankAccount;
import eBank.DTO.KontoDTO;

public interface BankAccountServiceIF {
    BankAccount getBankAccountByIban(String iban);

    BankAccount handleNewBuyOrder(BankAccount bankAccount, double amount);

    BankAccount handleNewSellOrder(BankAccount bankAccount, double amount);

    BankAccount saveBankAccount(BankAccount bankAccount);

    void updateBankAccountBalance(String iban, KontoDTO konto);
}
