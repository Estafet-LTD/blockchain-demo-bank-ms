package com.estafet.blockchain.demo.bank.ms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.blockchain.demo.bank.ms.model.WalletAddress;

@Service
public class BlockchainGatewayService {

	@Autowired
	private RestTemplate restTemplate;

	public WalletAddress generateWalletAddress() {
		return restTemplate.postForObject("BLOCKCHAIN_GATEWAY_MS_SERVICE_URI" + "/generate-wallet-account", null,
				WalletAddress.class);
	}

}
