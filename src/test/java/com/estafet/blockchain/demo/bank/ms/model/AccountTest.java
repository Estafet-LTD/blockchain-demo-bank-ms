package com.estafet.blockchain.demo.bank.ms.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccountTest {

	@Test
	public void testGetBalance() {
		Account account = new Account();
		account.credit(100).setId(0);
		account.credit(50).setId(1);
		account.debit(10).setId(2);
		assertEquals(150, account.getBalance(), 0);
	}

	@Test
	public void testGetPendingBalance() {
		Account account = new Account();
		account.credit(100).setId(0);
		account.credit(50).setId(1);
		account.debit(10).setId(2);
		assertEquals(-10, account.getPendingBalance(), 0);
	}

}
