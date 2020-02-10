package com.estafet.blockchain.demo.bank.ms.container.tests;

import com.estafet.openshift.boost.commons.lib.jms.TopicProducer;

public class BankPaymentTopicProducer extends TopicProducer {

	public BankPaymentTopicProducer() {
		super("bank.payment.topic");
	}
	
	public static void send(String message) {
		new BankPaymentTopicProducer().sendMessage(message);
	}

}
