package com.estafet.blockchain.demo.bank.ms.container.tests;

import com.estafet.openshift.boost.commons.lib.jms.TopicProducer;

public class BankPaymentConfirmationTopicProducer extends TopicProducer {

	public BankPaymentConfirmationTopicProducer() {
		super("bank.payment.confirmation.topic");
	}
	
	public static void send(String message) {
		new BankPaymentConfirmationTopicProducer().sendMessage(message);
	}

}
