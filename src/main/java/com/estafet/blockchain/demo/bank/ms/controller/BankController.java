package com.estafet.blockchain.demo.bank.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.blockchain.demo.bank.ms.model.API;
import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Money;
import com.estafet.blockchain.demo.bank.ms.service.AccountService;

@RestController
public class BankController {

	@Value("${app.version}")
	private String appVersion;

	@Autowired
	private AccountService accountService;

	@GetMapping("/api")
	public API getAPI() {
		return new API(appVersion);
	}

	@GetMapping(value = "/account/{id}")
	public Account getAccount(@PathVariable int id) {
		return accountService.getAccount(id);
	}
	
	@GetMapping(value = "/account/walletAddress/{walletAddress}")
	public Account getAccount(@PathVariable String walletAddress) {
		return accountService.getAccountByWalletAddress(walletAddress);
	}

	@PostMapping(value = "/account")
	public ResponseEntity<Account> createAccount(@RequestBody Account account) {
		return new ResponseEntity<Account>(accountService.createAccount(account), HttpStatus.OK);
	}

	@PostMapping(value = "/account/{id}/credit")
	public ResponseEntity<Account> credit(@PathVariable int id, @RequestBody Money money) {
		return new ResponseEntity<Account>(accountService.credit(id, money), HttpStatus.OK);
	}
	
	@PostMapping(value = "/account/{id}/debit")
	public ResponseEntity<Account> debit(@PathVariable int id, @RequestBody Money money) {
		return new ResponseEntity<Account>(accountService.debit(id, money), HttpStatus.OK);
	}
	
	@GetMapping(value = "/accounts")
	public List<Account> getAccounts() {
		return accountService.getAccounts();
	}
}
