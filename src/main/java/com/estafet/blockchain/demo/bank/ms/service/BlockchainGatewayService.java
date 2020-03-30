package com.estafet.blockchain.demo.bank.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.blockchain.demo.bank.ms.model.WalletAddress;
import org.web3j.crypto.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;

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
