package com.estafet.blockchain.demo.bank.ms.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccountTest {

	@Test
	public void testGetBalance() {
		Account account = new Account();
		account.credit(new Money(100));
		account.credit(new Money(50));
		assertFalse(account.isPending());
		account.debit(new Money(10));
		assertEquals(150, account.getBalance(), 0);
		assertTrue(account.isPending());
	}

	@Test
	public void testGetPendingBalance() {
		Account account = new Account();
		account.credit(new Money(100));
		account.credit(new Money(50));
		assertFalse(account.isPending());
		account.debit(new Money(10));
		assertEquals(-10, account.getPendingBalance(), 0);
		assertTrue(account.isPending());
	}

}
