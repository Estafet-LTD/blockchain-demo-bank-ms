package com.estafet.blockchain.demo.bank.ms.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.couchbase.client.java.repository.annotation.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

	@NotNull
	@Field("transaction_id")
	private Integer id;

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
				Objects.equals(id, that.id) &&
				Objects.equals(walletTransactionId, that.walletTransactionId) &&
				Objects.equals(status, that.status) &&
				Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, walletTransactionId, amount, status, description);
	}

	public boolean isCleared() {
		return status.equals("CLEARED");
	}

	public boolean isPending() {
		return status.equals("PENDING");
	}
}
