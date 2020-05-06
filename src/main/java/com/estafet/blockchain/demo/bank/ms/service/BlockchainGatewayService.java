package com.estafet.blockchain.demo.bank.ms.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;

import com.estafet.blockchain.demo.bank.ms.model.WalletAddress;

@Service
public class BlockchainGatewayService {

	public WalletAddress generateWalletAddress() {
		try {
			String seed = UUID.randomUUID().toString();
			ECKeyPair ecKeyPair = Keys.createEcKeyPair();
			WalletFile aWallet = Wallet.createLight(seed, ecKeyPair);
			String sPrivatekeyInHex = ecKeyPair.getPrivateKey().toString(16);
			Credentials.create(sPrivatekeyInHex);
			return new WalletAddress(aWallet.getAddress());
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException
				| CipherException e) {
			throw new RuntimeException(e);
		}
	}

}
