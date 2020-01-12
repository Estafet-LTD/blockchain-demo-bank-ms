package com.estafet.blockchain.demo.bank.ms.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estafet.blockchain.demo.bank.ms.dao.AccountDAO;
import com.estafet.blockchain.demo.bank.ms.jms.CurrencyConverterProducer;
import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Money;
import com.estafet.blockchain.demo.bank.ms.model.Wallet;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;

@Service
public class AccountService {

	@Autowired
	private AccountDAO accountDAO;

	@Autowired
	private CurrencyConverterProducer currencyConverterProducer;

	@Transactional(readOnly = true)
	public Account getAccount(Integer accountId) {
		return accountDAO.getAccount(accountId);
	}

	@Transactional
	public Account createAccount(Wallet wallet) {
		return accountDAO.createAccount(Account.instance(wallet));
	}

	@Transactional
	public Account credit(int accountId, Money money) {
		Account account = accountDAO.getAccount(accountId);
		account.credit(money);
		accountDAO.updateAccount(account);
		return account;
	}

	@Transactional
	public Account debit(int accountId, Money money) {
		Account account = accountDAO.getAccount(accountId);
		account.debit(money);
		accountDAO.updateAccount(account);
		return account;
	}

	@Transactional
	public void handleBankPaymentMessage(BankPaymentMessage message) {
		Account account = accountDAO.getAccountByWalletAddress(message.getWalletAddress());
		account.debit(new Money(message.getTransactionId(), message.getAmount()));
		accountDAO.updateAccount(account);
		currencyConverterProducer.sendMessage(new BankPaymentCurrencyConverterMessage(message.getAmount(),
				account.getCurrency(), message.getWalletAddress(), "ddhshs", message.getTransactionId()));
	}

	public Account getAccountByWalletAddress(String walletAddress) {
		return accountDAO.getAccountByWalletAddress(walletAddress);
	}

}
