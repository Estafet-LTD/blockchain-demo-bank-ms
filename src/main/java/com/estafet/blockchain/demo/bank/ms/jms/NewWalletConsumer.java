package com.estafet.blockchain.demo.bank.ms.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.blockchain.demo.bank.ms.event.MessageEventHandler;
import com.estafet.blockchain.demo.bank.ms.model.Wallet;
import com.estafet.blockchain.demo.bank.ms.service.AccountService;

import io.opentracing.Tracer;

@Component
public class NewWalletConsumer {

	public final static String TOPIC = "new.wallet.topic";
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MessageEventHandler messageEventHandler;

	@Transactional
	@JmsListener(destination = TOPIC, containerFactory = "myFactory")
	public void onMessage(String message, @Header("message.event.interaction.reference") String reference) {
		try {
			if (messageEventHandler.isValid(TOPIC, reference)) {
				accountService.createAccount(Wallet.fromJSON(message));
			}
		} finally {
			if (tracer.activeSpan() != null) {
				tracer.activeSpan().close();	
			}
		}
	}

}
