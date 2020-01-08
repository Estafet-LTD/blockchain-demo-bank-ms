package com.estafet.blockchain.demo.bank.ms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {

	@Id
	@Column(name = "TRANSACTION_ID")
	private Integer id;
	
	@Column(name = "WALLET_TRANSACTION_ID", nullable = false)
	private String walletTransactionId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", nullable = false, referencedColumnName = "ACCOUNT_ID", foreignKey = @ForeignKey(name = "TRANSACTION_TO_ACCOUNT_FK"))
	private Account transactionAccount;
	
	@Column(name = "AMOUNT", nullable = false)
	private double amount = 0;
	
	@Column(name = "CLEARED", nullable = false)
	private boolean cleared = true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWalletTransactionId() {
		return walletTransactionId;
	}

	public void setWalletTransactionId(String walletTransactionId) {
		this.walletTransactionId = walletTransactionId;
	}

	public Account getTransactionAccount() {
		return transactionAccount;
	}

	public void setTransactionAccount(Account transactionAccount) {
		this.transactionAccount = transactionAccount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isCleared() {
		return cleared;
	}

	public void setCleared(boolean cleared) {
		this.cleared = cleared;
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
		Transaction other = (Transaction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
