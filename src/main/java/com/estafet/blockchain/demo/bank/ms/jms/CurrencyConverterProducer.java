package com.estafet.blockchain.demo.bank.ms.jms;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;

@Component
public class CurrencyConverterProducer {

	@Autowired 
	private JmsTemplate jmsTemplate;
	
	public void sendMessage(BankPaymentCurrencyConverterMessage message) {
		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.convertAndSend("currency.converter.input.topic", message.toJSON(), new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				message.setStringProperty("message.event.interaction.reference", UUID.randomUUID().toString());
				return message;
			}
		});
	}
}
