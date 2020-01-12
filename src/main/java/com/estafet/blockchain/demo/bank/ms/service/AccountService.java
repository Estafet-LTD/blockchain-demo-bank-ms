package com.estafet.blockchain.demo.bank.ms.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estafet.blockchain.demo.bank.ms.dao.AccountDAO;
import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import com.estafet.blockchain.demo.bank.ms.model.Wallet;

@Service
public class AccountService {

	@Autowired
	private AccountDAO accountDAO;
	
	@Transactional(readOnly = true)
	public Account getAccount(Integer accountId) {
		return accountDAO.getAccount(accountId);
	}

	@Transactional
	public Account createAccount(String currency, Wallet wallet) {
		Account account = Account.instance(wallet);
		account.setCurrency(currency);
		return accountDAO.createAccount(account);
	}

	@Transactional
	public Transaction addTransaction(int accountId, Transaction transaction) {
		Account account = accountDAO.getAccount(accountId);
		account.addTransaction(transaction);
		accountDAO.update(account);
		return transaction;
	}
	
}
