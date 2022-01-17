package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.BankAccount;

public interface BankClientIF {
    BankAccount createBankAccount(BankAccount bankAccount);
}
