package com.estafet.blockchain.demo.bank.ms.controller;

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
import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import com.estafet.blockchain.demo.bank.ms.model.Wallet;
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

	@PostMapping(value = "/account/currency/{currency}")
	public ResponseEntity<Account> createAccount(@PathVariable String currency, @RequestBody Wallet wallet) {
		return new ResponseEntity<Account>(accountService.createAccount(currency, wallet), HttpStatus.OK);
	}

	@PostMapping(value = "/account/{id}/transaction")
	public ResponseEntity<Transaction> addTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
		return new ResponseEntity<Transaction>(accountService.addTransaction(id, transaction), HttpStatus.OK);
	}
}
