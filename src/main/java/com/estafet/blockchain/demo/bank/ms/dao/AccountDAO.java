package com.estafet.blockchain.demo.bank.ms.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.estafet.blockchain.demo.bank.ms.model.Account;

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
	
	public Account getAccountByWalletAddress(String walletAddress) {
		TypedQuery<Account> query = entityManager.createQuery("select a from Account a where a.walletAddress = ?1", Account.class);
		return query.setParameter(1, walletAddress).getSingleResult();
	}
	
}
