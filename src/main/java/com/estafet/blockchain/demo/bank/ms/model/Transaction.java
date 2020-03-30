package com.estafet.blockchain.demo.bank.ms.model;

import com.couchbase.client.java.repository.annotation.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class Transaction implements Serializable {

	@NotNull
	@Field
	private String walletTransactionId;

	@NotNull
	@Field
	private double amount = 0;

	@NotNull
	@Field
	private String status = "CLEARED";

	@NotNull
	@Field
	private String description;

	public Transaction(){

	}

	public Transaction(String walletTransactionId, double amount, String status, String description) {
		this.walletTransactionId = walletTransactionId;
		this.amount = amount;
		this.status = status;
		this.description = description;
	}

	public String getWalletTransactionId() {
		return walletTransactionId;
	}

	public void setWalletTransactionId(String walletTransactionId) {
		this.walletTransactionId = walletTransactionId;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Transaction that = (Transaction) o;
		return Double.compare(that.amount, amount) == 0 &&
				Objects.equals(walletTransactionId, that.walletTransactionId) &&
				Objects.equals(status, that.status) &&
				Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(walletTransactionId, amount, status, description);
	}

	public boolean isCleared() {
		return status.equals("CLEARED");
	}

	public boolean isPending() {
		return status.equals("PENDING");
	}
}
