package com.estafet.blockchain.demo.bank.ms.service;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Money;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentConfirmationMessage;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;

import java.util.List;

public interface AccountService {

	Account getAccount(String accountId);

	void deleteAll();

	Account createAccount(Account account);

	Account credit(String accountId, Money money);

	Account debit(String accountId, Money money);

	void handleBankPaymentMessage(BankPaymentMessage message);

	Account getAccountByWalletAddress(String walletAddress);

	List<Account> getAccounts();

	void handleBankPaymentConfirmationMessage(BankPaymentConfirmationMessage message);
}
