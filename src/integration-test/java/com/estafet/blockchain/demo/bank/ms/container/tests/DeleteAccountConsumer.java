package com.estafet.blockchain.demo.bank.ms.container.tests;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.boostcd.jms.TopicConsumer;

public class DeleteAccountConsumer extends TopicConsumer {

	public DeleteAccountConsumer() {
		super("delete.account.topic");
	}

	public Account consume() {
		return super.consume(Account.class);
	}

}
