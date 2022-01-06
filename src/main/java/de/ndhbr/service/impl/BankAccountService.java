package de.ndhbr.service.impl;

import de.ndhbr.entity.BankAccount;
import de.ndhbr.repository.BankAccountRepo;
import de.ndhbr.service.BankAccountServiceIF;
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
