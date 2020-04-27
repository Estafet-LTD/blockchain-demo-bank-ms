package com.estafet.blockchain.demo.bank.ms.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class Account implements Serializable {

	@Id
	private String id = String.valueOf((int)(999999999.0 * Math.random()));

	@NotNull
	@Field
	private String walletAddress;

	@NotNull
	@Field
	private String accountName;

	@NotNull
	@Field
	private String currency;

	@Field
	private Set<Transaction> transactions = new HashSet<Transaction>();

	public Account(){

	}

	public Account(String id, String walletAddress, String accountName, String currency, Set<Transaction> transactions) {
		this.id = id;
		this.walletAddress = walletAddress;
		this.accountName = accountName;
		this.currency = currency;
		this.transactions = transactions;
	}

	public double getBalance() {
		double balance = 0;
		for (Transaction transaction : transactions) {
			if (transaction.isCleared()) {
				balance += transaction.getAmount();
			}
		}
		return balance;
	}
	
	public double getPendingBalance() {
		double balance = 0;
		for (Transaction transaction : transactions) {
			if (!transaction.isCleared()) {
				balance += transaction.getAmount();	
			}
		}
		return balance;
	}
	
	public boolean isPending() {
		for (Transaction transaction : transactions) {
			if (transaction.isPending()) {
				return true;
			}
		}
		return false;
	}

	public Transaction debit(Money money) {
		if ((getBalance() - money.getAmount()) < 0) {
			throw new RuntimeException("Insufficient Funds");
		}
		Transaction tx = new Transaction();
		tx.setWalletTransactionId(money.getWalletTransactionId());
		tx.setAmount(money.getAmount() * -1.0d);
		tx.setId(this.getTransactions().size()+1);
		tx.setStatus("PENDING");
		if (money.getWalletTransactionId() != null) {
			tx.setDescription("Wallet Transfer to with transfer id - " + money.getWalletTransactionId());
		} else {
			tx.setDescription("User Withdrawal");
		}
		addTransaction(tx);
		return tx;
	}

	public Transaction credit(Money money) {
		Transaction tx = new Transaction();
		tx.setWalletTransactionId(money.getWalletTransactionId());
		tx.setAmount(money.getAmount());
		tx.setId(this.getTransactions().size()+1);
		if (transactions.isEmpty()) {
			tx.setDescription("Opening Deposit");
		} else {
			tx.setDescription("User Deposit");
		}
		addTransaction(tx);
		return tx;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	private Account addTransaction(Transaction transaction) {
		transactions.add(transaction);
		return this;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static Account fromJSON(String message) {
		try {
			return (Account)(new ObjectMapper()).readValue(message, Account.class);
		} catch (IOException var2) {
			throw new RuntimeException(var2);
		}
	}

	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
