package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.repository.BankAccountRepo;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService implements BankAccountServiceIF {

    @Autowired
    BankAccountRepo bankAccountRepo;

    @Override
    public BankAccount getBankAccountByIban(String iban) {
        return bankAccountRepo.getById(iban);
    }

    @Override
    public BankAccount handleNewBuyOrder(BankAccount bankAccount, double amount) {
        bankAccount.decreaseVirtualBalance(amount);
        bankAccount.decreaseVirtualBalance(0.99);
        return saveBankAccount(bankAccount);
    }

    @Override
    public BankAccount handleNewSellOrder(BankAccount bankAccount, double amount) {
        bankAccount.increaseVirtualBalance(amount);
        bankAccount.decreaseVirtualBalance(0.99);
        return saveBankAccount(bankAccount);
    }

    @Override
    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return bankAccountRepo.save(bankAccount);
    }
}