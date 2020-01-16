package com.estafet.blockchain.demo.bank.ms.jms;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
public class NewAccountProducer {

    public final static String TOPIC = "new.account.topic";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(Account account) {
        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend(TOPIC, account.toJSON(), new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("message.event.interaction.reference", UUID.randomUUID().toString());
                return message;
            }
        });
    }
}
