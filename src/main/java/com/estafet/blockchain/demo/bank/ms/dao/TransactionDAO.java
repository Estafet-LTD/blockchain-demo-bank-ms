package com.estafet.blockchain.demo.bank.ms.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.estafet.blockchain.demo.bank.ms.model.Transaction;

@Repository
public class TransactionDAO {

	@PersistenceContext
	private EntityManager entityManager;

	public void updateTransaction(Transaction transaction) {
		entityManager.merge(transaction);
	}
	
	public Transaction getTransactionByWalletTransactionId(String walletTransactionId) {
		TypedQuery<Transaction> query = entityManager.createQuery("select t from Transaction t where t.walletTransactionId = ?1", Transaction.class);
		return query.setParameter(1, walletTransactionId).getSingleResult();
	}
	
}
