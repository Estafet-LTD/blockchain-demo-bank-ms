package com.estafet.blockchain.demo.bank.ms.service;

import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import com.estafet.blockchain.demo.bank.ms.repository.TransactionRepository;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentConfirmationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void handleBankPaymentConfirmationMessage(BankPaymentConfirmationMessage message) {
        Transaction transaction = transactionRepository.findByWalletTransactionId(message.getTransactionId());
        transaction.setStatus("CLEARED");
        transactionRepository.getCouchbaseOperations().update(transaction);
    }
}
