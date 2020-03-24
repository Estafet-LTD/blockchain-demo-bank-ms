package com.estafet.blockchain.demo.bank.ms.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
public class Transaction {

	@Id
	@NotNull
	@Field("transaction_id")
	private Integer id;

	@NotNull
	@Field("wallet_transaction_id")
	private String walletTransactionId;

	@JsonIgnore
/*	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", nullable = false, referencedColumnName = "ACCOUNT_ID", foreignKey = @ForeignKey(name = "TRANSACTION_TO_ACCOUNT_FK"))*/
	@NotNull
	@Field
	private Account transactionAccount;

	@NotNull
	private double amount = 0;

	@NotNull
	@Field
	private String status = "CLEARED";

	@NotNull
	@Field
	private String description;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public boolean isCleared() {
		return status.equals("CLEARED");
	}

	public boolean isPending() {
		return status.equals("PENDING");
	}
}
