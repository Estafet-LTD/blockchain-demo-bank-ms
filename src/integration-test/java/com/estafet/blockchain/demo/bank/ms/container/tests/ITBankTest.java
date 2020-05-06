package com.estafet.blockchain.demo.bank.ms.container.tests;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.estafet.openshift.boost.commons.lib.properties.PropertyUtils;
import com.estafet.openshift.boost.couchbase.lib.annotation.BucketSetup;
import com.estafet.openshift.boost.couchbase.lib.spring.CouchbaseTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, CouchbaseTestExecutionListener.class })
public class ITBankTest {

	CurrencyConverterConsumer currencyConverterTopic = new CurrencyConverterConsumer();
	NewAccountConsumer newAccountTopic = new NewAccountConsumer();
	DeleteAccountConsumer deleteAccountTopic = new DeleteAccountConsumer();
	
	@Before
	public void before() {
		RestAssured.baseURI = PropertyUtils.instance().getProperty("BANK_MS_SERVICE_URI");
	}

	@After
	public void after() {
		currencyConverterTopic.closeConnection();
		newAccountTopic.closeConnection();
		deleteAccountTopic.closeConnection();
	}

	@Test
	@BucketSetup("ITBankTest.json")
	public void testGetAccount() {
		get("/account/1000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is("1000"))
			.body("walletAddress", is("abcd"))
			.body("accountName", is("Dennis"))
			.body("currency", is("USD"))
			.body("balance",is(150.0f))
			.body("pendingBalance",is(0.0f))
			.body("pending", is(false));		
	}

	@Test
	@BucketSetup("ITBankTest.json")
	public void testCredit() {
		given().contentType(ContentType.JSON)
			.body("{ \"amount\": 7800.67 }")
			.when()
				.post("/account/2000/credit")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is("2000"))
				.body("balance", is(13200.67f))
				.body("pending", is(false));
	}

	@Test
	@BucketSetup("ITBankTest.json")
	public void testDebit() {
		given().contentType(ContentType.JSON)
			.body("{ \"amount\": 20.0 }")
			.when()
				.post("/account/1000/debit")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is("1000"))
				.body("currency", is("USD"))
				.body("balance", is(150.00f))
				.body("pendingBalance", is(-20.00f))
				.body("pending", is(true));
	}
	
	@Test
	@BucketSetup("ITBankTest.json")
	public void testCreateAccount() {
		given().contentType(ContentType.JSON)
			.body("{\"accountName\": \"Peter\", \"currency\": \"EUR\" }")
			.when()
				.post("/account")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is(notNullValue()))
				.body("walletAddress", is(not(nullValue())))
				.body("accountName", is("Peter"))
				.body("currency", is("EUR"));
		
		Account account = newAccountTopic.consume();
		assertEquals("Peter", account.getAccountName());
		assertEquals("EUR", account.getCurrency());
	}

	@Test
	@BucketSetup("ITBankTest.json")
	public void testGetAccounts() {
		get("/accounts").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", hasItems("1000", "2000"))
			.body("currency",  hasItems("USD", "GBP"));
	}
	
	@Test
	@BucketSetup("ITBankTest.json")
	public void testDeleteAccounts() {
		delete("accounts").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", hasItems("1000", "2000"))
			.body("currency",  hasItems("USD", "GBP"));
		
		Account account1 = deleteAccountTopic.consume();
		assertTrue(account1.getId().equals("1000") || account1.getId().equals("2000"));
		Account account2 = deleteAccountTopic.consume();
		assertTrue(account2.getId().equals("1000") || account2.getId().equals("2000"));
		
		get("/accounts").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", hasSize(0));
	}
	
	@Test
	@BucketSetup("ITBankTest.json")
	public void testConsumeBankPayment() {
		BankPaymentTopicProducer.send("{\"walletAddress\":\"efgh\",\"amount\":200.65,\"transactionId\":\"123456\"}");
		BankPaymentCurrencyConverterMessage message = currencyConverterTopic.consume();
		assertEquals("GBP", message.getCurrency());
		assertEquals("123456", message.getTransactionId());
		assertEquals("efgh", message.getWalletAddress());
		assertEquals(200.65, message.getCurrencyAmount(), 0);
	}
	
	@Test
	@BucketSetup("ITBankTest.json")
	public void testConsumeBankPaymentConfirmation() {
		get("/account/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is("2000"))
			.body("pending", is(false));	
		
		BankPaymentTopicProducer.send("{\"walletAddress\":\"efgh\",\"amount\":200.65,\"transactionId\":\"123456\"}");
		currencyConverterTopic.consume();
		
		get("/account/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is("2000"))
			.body("pending", is(true));		
		
		BankPaymentConfirmationTopicProducer.send("{\"status\":\"SUCCESS\",\"signature\":\"sjsjsjsjs\",\"transactionId\":\"123456\"}");
		
		get("/account/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is("2000"))
			.body("pending", is(false));			
	}

}
