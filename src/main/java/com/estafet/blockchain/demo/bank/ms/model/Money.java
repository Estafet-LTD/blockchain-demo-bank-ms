package com.estafet.blockchain.demo.bank.ms.model;

public class Money {

	private String walletTransactionId;

	private double amount;

	public Money() {
	}

	public Money(double amount) {
		this.amount = amount;
	}

	public Money(String walletTransactionId, double amount) {
		this.walletTransactionId = walletTransactionId;
		this.amount = amount;
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

}
