package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import org.hibernate.service.spi.ServiceException;

public interface BankClientIF {

    /**
     * Transfer money from one bank account to another
     * @param ibanSender Sender IBAN
     * @param ibanReceiver Receiver IBAN
     * @param amount Amount of Money
     * @param purposeOfUse Comment
     * @throws ServiceUnavailableException If bank is not available
     */
    void createTransfer(String ibanSender, String ibanReceiver, double amount,
                        String purposeOfUse) throws ServiceUnavailableException;

    /**
     * Creates bank account
     * @param bankAccount Bank Account
     * @param customer Customer
     * @return Bank Account with IBAN, username and password
     * @throws ServiceUnavailableException If bank is not available
     * @throws ServiceException If other error occurs
     */
    BankAccount createBankAccount(BankAccount bankAccount, Customer customer)
            throws ServiceUnavailableException, ServiceException;
}
