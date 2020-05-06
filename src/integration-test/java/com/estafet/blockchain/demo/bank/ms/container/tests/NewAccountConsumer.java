package com.estafet.blockchain.demo.bank.ms.container.tests;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.openshift.boost.commons.lib.jms.TopicConsumer;

public class NewAccountConsumer extends TopicConsumer {

	public NewAccountConsumer() {
		super("new.account.topic");
	}

	public Account consume() {
		return super.consume(Account.class);
	}

}
