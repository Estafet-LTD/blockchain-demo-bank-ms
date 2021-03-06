package com.estafet.blockchain.demo.bank.ms.service;

import com.estafet.blockchain.demo.bank.ms.jms.CurrencyConverterProducer;
import com.estafet.blockchain.demo.bank.ms.jms.DeleteAccountProducer;
import com.estafet.blockchain.demo.bank.ms.jms.NewAccountProducer;
import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Money;
import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import com.estafet.blockchain.demo.bank.ms.repository.AccountRepository;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentConfirmationMessage;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

	@Autowired
    private CurrencyConverterProducer currencyConverterProducer;

	@Autowired
    private BlockchainGatewayService blockchainGatewayService;

	@Autowired
    private AccountRepository accountRepository;

	@Autowired
    private NewAccountProducer newAccountProducer;
	
	@Autowired
	private DeleteAccountProducer deleteAccountProducer;

    @Override
    public Account getAccount(String accountId) {
        return accountRepository.findOne(accountId);
    }

    @Override
	public Account delete(String accountId) {
    	Account account = getAccount(accountId);
    	accountRepository.delete(accountId);
    	deleteAccountProducer.sendMessage(account);
    	return account;
	}

	@Override
    public Account[] deleteAll() {
		List<Account> accounts = accountRepository.findAll();
		for (Account account : accounts) {
			delete(account.getId());	
		}
		return accounts.toArray(new Account[accounts.size()]);
    }

    @Override
    public Account createAccount(Account account) {
        account.setWalletAddress(blockchainGatewayService.generateWalletAddress().getAddress());
        account.setId(String.valueOf((int)(999999999.0 * Math.random())));
        accountRepository.save(account);
        newAccountProducer.sendMessage(account);
        return account;
    }

    @Override
    public Account credit(String accountId, Money money) {
        Account account = accountRepository.findOne(accountId);
        account.credit(money);
        accountRepository.getCouchbaseOperations().update(account);
        return account;
    }

    @Override
    public Account debit(String accountId, Money money) {
        Account account = accountRepository.findOne(accountId);
        account.debit(money);
        accountRepository.getCouchbaseOperations().update(account);
        return account;
    }

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

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public void handleBankPaymentConfirmationMessage(BankPaymentConfirmationMessage message) {
        Account account = accountRepository.findByWalletTransactionId(message.getTransactionId());

        if(account!=null && account.getTransactions().size()!=0){
            for(Transaction transaction: account.getTransactions()){
                if(transaction.getWalletTransactionId()!=null && transaction.getWalletTransactionId().equals(message.getTransactionId())){
                    transaction.setStatus("CLEARED");
                    accountRepository.getCouchbaseOperations().update(account);
                }
            }
        }

    }

}
