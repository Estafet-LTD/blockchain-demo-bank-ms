package com.estafet.blockchain.demo.bank.ms.service;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentConfirmationMessage;

public interface TransactionService {

	void handleBankPaymentConfirmationMessage(BankPaymentConfirmationMessage message);
}
