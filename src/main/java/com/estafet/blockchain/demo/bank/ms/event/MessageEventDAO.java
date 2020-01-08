package com.estafet.blockchain.demo.bank.ms.event;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.blockchain.demo.bank.ms.model.MessageEvent;

@Repository
public class MessageEventDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	public MessageEvent getMessageEvent(String topic) {
		return entityManager.find(MessageEvent.class, topic);
	}
	
	@Transactional
	public void create(MessageEvent abstractMessageEvent) {
		entityManager.persist(abstractMessageEvent);
	}
	
	@Transactional
	public void update(MessageEvent abstractMessageEvent) {
		entityManager.merge(abstractMessageEvent);
	}

}
