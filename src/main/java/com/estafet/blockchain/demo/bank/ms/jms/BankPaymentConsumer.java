package com.estafet.blockchain.demo.bank.ms.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.estafet.blockchain.demo.bank.ms.service.AccountService;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentMessage;

import io.opentracing.Tracer;

@Component
public class BankPaymentConsumer {

	public final static String TOPIC = "bank.payment.topic";
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private AccountService accountService;

	@Transactional
	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message) {
		try {
			accountService.handleBankPaymentMessage(BankPaymentMessage.fromJSON(message));
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();	
			}
		}
	}

}
