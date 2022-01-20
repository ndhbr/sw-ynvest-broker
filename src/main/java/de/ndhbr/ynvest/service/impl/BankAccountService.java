package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.repository.BankAccountRepo;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import de.ndhbr.ynvest.util.Constants;
import eBank.DTO.KontoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Scope("singleton")
public class BankAccountService implements BankAccountServiceIF {

    @Autowired
    BankAccountRepo bankAccountRepo;

    @Autowired
    OrderServiceIF orderService;

    @Override
    public BankAccount getBankAccountByIban(String iban) throws EntityNotFoundException {
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

    @Override
    public void updateBankAccountBalance(String iban, KontoDTO konto) {
        BankAccount foundBankAccount = getBankAccountByIban(iban);
        double newVirtualBalance = konto.getKontostand() -
                orderService.getSumOfOpenOrders(foundBankAccount.getCustomer());

        foundBankAccount.setBalance(konto.getKontostand());
        foundBankAccount.setVirtualBalance(newVirtualBalance);

        saveBankAccount(foundBankAccount);
    }
}
