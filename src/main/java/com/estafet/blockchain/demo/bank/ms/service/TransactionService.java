package com.estafet.blockchain.demo.bank.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.blockchain.demo.bank.ms.dao.TransactionDAO;
import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentConfirmationMessage;

@Service
public class TransactionService {

	@Autowired
	private TransactionDAO transactionDAO;

	@Transactional
	public void handleBankPaymentConfirmationMessage(BankPaymentConfirmationMessage message) {
		Transaction transaction = transactionDAO.getTransactionByWalletTransactionId(message.getTransactionId());
		transaction.setStatus("CLEARED");
		transactionDAO.updateTransaction(transaction);
	}

}
