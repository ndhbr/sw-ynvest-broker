package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.repository.BankAccountRepo;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
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
        bankAccount.decreaseVirtualBalance(Constants.TRANSACTION_FEE);
        return saveBankAccount(bankAccount);
    }

    @Override
    public BankAccount handleNewSellOrder(BankAccount bankAccount, double amount) {
        bankAccount.increaseVirtualBalance(amount);
        bankAccount.decreaseVirtualBalance(Constants.TRANSACTION_FEE);
        return saveBankAccount(bankAccount);
    }

    @Override
    public BankAccount saveBankAccount(BankAccount bankAccount) {
        return bankAccountRepo.save(bankAccount);
    }
}
