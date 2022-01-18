package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import org.hibernate.service.spi.ServiceException;

public interface BankClientIF {

    void createTransfer(String ibanSender, String ibanReceiver, double amount,
                        String purposeOfUse) throws ServiceUnavailableException;

    BankAccount createBankAccount(BankAccount bankAccount, Customer customer)
            throws ServiceUnavailableException, ServiceException;
}
