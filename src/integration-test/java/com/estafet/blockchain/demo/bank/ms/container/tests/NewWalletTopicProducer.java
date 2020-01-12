package com.estafet.blockchain.demo.bank.ms.container.tests;

import com.estafet.microservices.scrum.lib.commons.jms.TopicProducer;

public class NewWalletTopicProducer extends TopicProducer {

	public NewWalletTopicProducer() {
		super("new.wallet.topic");
	}
	
	public static void send(String message) {
		new NewWalletTopicProducer().sendMessage(message);
	}

}
