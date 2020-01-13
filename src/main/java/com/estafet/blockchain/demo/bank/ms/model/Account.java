package com.estafet.blockchain.demo.bank.ms.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
public class Account {

	@Id
	@SequenceGenerator(name = "ACCOUNT_ID_SEQ", sequenceName = "ACCOUNT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_ID_SEQ")
	@Column(name = "ACCOUNT_ID")
	private Integer id;

	@Column(name = "WALLET_ADDRESS", nullable = false)
	private String walletAddress;

	@Column(name = "ACCOUNT_NAME", nullable = false)
	private String accountName;

	@Column(name = "PUBLIC_KEY", nullable = false)
	private String publicKey;

	@Column(name = "CURRENCY", nullable = false)
	private String currency;

	@OneToMany(mappedBy = "transactionAccount", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Transaction> transactions = new HashSet<Transaction>();

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
		Transaction tx = new Transaction();
		tx.setWalletTransactionId(money.getWalletTransactionId());
		tx.setAmount(money.getAmount() * -1.0d);
		tx.setStatus("PENDING");
		addTransaction(tx);
		return tx;
	}

	public Transaction credit(Money money) {
		Transaction tx = new Transaction();
		tx.setWalletTransactionId(money.getWalletTransactionId());
		tx.setAmount(money.getAmount());
		addTransaction(tx);
		return tx;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	private Account addTransaction(Transaction transaction) {
		transaction.setTransactionAccount(this);
		transactions.add(transaction);
		return this;
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

	public static Account instance(Wallet wallet) {
		Account account = new Account();
		account.setAccountName(wallet.getWalletName());
		account.setWalletAddress(wallet.getWalletAddress());
		account.setCurrency(wallet.getCurrency());
		account.setPublicKey("sjsjsj");
		return account;
	}

}
