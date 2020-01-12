package com.estafet.blockchain.demo.bank.ms.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Transaction;

@Repository
public class AccountDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Account getAccount(Integer accountId) {
		return entityManager.find(Account.class, accountId);
	}

	public Account createAccount(Account account) {
		entityManager.persist(account);
		return account;
	}

	public void updateAccount(Account account) {
		entityManager.merge(account);
	}

	public void saveTransaction(Transaction transaction) {
		entityManager.persist(transaction);
	}
	
}
