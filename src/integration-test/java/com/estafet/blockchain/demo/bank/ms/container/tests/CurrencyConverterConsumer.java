package com.estafet.blockchain.demo.bank.ms.container.tests;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;
import com.estafet.openshift.boost.commons.lib.jms.TopicConsumer;

public class CurrencyConverterConsumer extends TopicConsumer {

	public CurrencyConverterConsumer() {
		super("currency.converter.input.topic");
	}

	public BankPaymentCurrencyConverterMessage consume() {
		return super.consume(BankPaymentCurrencyConverterMessage.class);
	}

}
