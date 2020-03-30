package com.estafet.blockchain.demo.bank.ms.jms;

import com.estafet.blockchain.demo.bank.ms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentConfirmationMessage;

import io.opentracing.Tracer;

@Component
public class BankPaymentConfirmationConsumer {

	public final static String TOPIC = "bank.payment.confirmation.topic";
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private AccountService accountService;

	@Transactional
	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message) {
		try {
			accountService.handleBankPaymentConfirmationMessage(BankPaymentConfirmationMessage.fromJSON(message));
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();	
			}
		}
	}

}
