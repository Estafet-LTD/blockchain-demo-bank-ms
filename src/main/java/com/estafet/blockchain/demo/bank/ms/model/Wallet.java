package com.estafet.blockchain.demo.bank.ms.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Wallet {

	private String walletAddress;

	private String walletName;

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public static Wallet fromJSON(String message) {
        try {
            return new ObjectMapper().readValue(message, Wallet.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
