package com.estafet.blockchain.demo.bank.ms.container.tests;

import com.estafet.blockchain.demo.messages.lib.wallet.NewWalletMessage;
import com.estafet.microservices.scrum.lib.commons.jms.TopicConsumer;

public class NewWalletTopicConsumer extends TopicConsumer {

	public NewWalletTopicConsumer() {
		super("new.wallet.topic");
	}

	public NewWalletMessage consume() {
		return super.consume(NewWalletMessage.class);
	}

}
