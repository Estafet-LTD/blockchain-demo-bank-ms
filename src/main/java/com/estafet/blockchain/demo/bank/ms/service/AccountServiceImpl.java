package com.estafet.blockchain.demo.bank.ms.service;

import com.estafet.blockchain.demo.bank.ms.jms.CurrencyConverterProducer;
import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Money;
import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import com.estafet.blockchain.demo.bank.ms.repository.AccountRepository;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentConfirmationMessage;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private CurrencyConverterProducer currencyConverterProducer;

    @Autowired
    private BlockchainGatewayService blockchainGatewayService;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public Account getAccount(String accountId) {
        return accountRepository.findOne(accountId);
    }

    @Transactional
    @Override
    public void deleteAll() {
        accountRepository.deleteAll();
    }

    @Transactional
    @Override
    public Account createAccount(Account account) {
        account.setWalletAddress(blockchainGatewayService.generateWalletAddress().getAddress());
        return accountRepository.save(account);
    }

    @Transactional
    @Override
    public Account credit(String accountId, Money money) {
        Account account = accountRepository.findOne(accountId);
        account.credit(money);
        accountRepository.getCouchbaseOperations().update(account);
        return account;
    }

    @Transactional
    @Override
    public Account debit(String accountId, Money money) {
        Account account = accountRepository.findOne(accountId);
        account.debit(money);
        accountRepository.getCouchbaseOperations().update(account);
        return account;
    }

    @Transactional
    @Override
    public void handleBankPaymentMessage(BankPaymentMessage message) {
        Account account = accountRepository.findByWalletAddress(message.getWalletAddress());
        account.debit(new Money(message.getTransactionId(), message.getAmount()));
        accountRepository.getCouchbaseOperations().update(account);
        currencyConverterProducer.sendMessage(new BankPaymentCurrencyConverterMessage(message.getAmount(),
                account.getCurrency(), message.getWalletAddress(), "ddhshs", message.getTransactionId()));
    }

    @Override
    public Account getAccountByWalletAddress(String walletAddress) {
        return accountRepository.findByWalletAddress(walletAddress);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Transactional
    @Override
    public void handleBankPaymentConfirmationMessage(BankPaymentConfirmationMessage message) {
        Account account = accountRepository.findByWalletTransactionId(message.getTransactionId());

        if(account!=null && account.getTransactions().size()!=0){
            for(Transaction transaction: account.getTransactions()){
                if(transaction.getWalletTransactionId().equals(message.getTransactionId())){
                    transaction.setStatus("CLEARED");
                    accountRepository.getCouchbaseOperations().update(account);
                }
            }
        }

    }
}
