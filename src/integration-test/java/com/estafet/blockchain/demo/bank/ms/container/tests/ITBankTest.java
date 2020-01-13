package com.estafet.blockchain.demo.bank.ms.container.tests;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;
import com.estafet.microservices.scrum.lib.commons.properties.PropertyUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ITBankTest {

	CurrencyConverterConsumer topic = new CurrencyConverterConsumer();
	
	@Before
	public void before() {
		RestAssured.baseURI = PropertyUtils.instance().getProperty("BANK_MS_SERVICE_URI");
	}

	@After
	public void after() {
		topic.closeConnection();
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testGetAccount() {
		get("/account/1000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(1000))
			.body("walletAddress", is("abcd"))
			.body("accountName", is("Dennis"))
			.body("publicKey", is("dddd"))
			.body("currency", is("USD"))
			.body("balance",is(150.0f))
			.body("pendingBalance",is(0.0f))
			.body("pending", is(true));		
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testCredit() {
		given().contentType(ContentType.JSON)
			.body("{ \"amount\": 7800.67 }")
			.when()
				.post("/account/2000/credit")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is(2000))
				.body("balance", is(13200.67f))
				.body("pending", is(false));
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testDedit() {
		given().contentType(ContentType.JSON)
			.body("{ \"amount\": 20.0 }")
			.when()
				.post("/account/1000/debit")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is(1000))
				.body("currency", is("USD"))
				.body("balance", is(150.00f))
				.body("pendingBalance", is(-20.00f))
				.body("pending", is(true));;
	}
	
	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testCreateAccount() {
		given().contentType(ContentType.JSON)
			.body("{\"walletAddress\": \"abcd\", \"walletName\": \"Peter\", \"currency\": \"EUR\" }")
			.when()
				.post("/account")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is(notNullValue()))
				.body("walletAddress", is("abcd"))
				.body("accountName", is("Peter"))
				.body("currency", is("EUR"));
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testConsumeNewWallet() {
		NewWalletTopicProducer.send("{\"walletAddress\":\"ssjsjaja\",\"walletName\":\"Dennis\",\"currency\":\"USD\"}");
		get("/account/walletAddress/ssjsjaja").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(notNullValue()));
	}
	
	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testConsumeBankPayment() {
		BankPaymentTopicProducer.send("{\"walletAddress\":\"efgh\",\"amount\":200.65,\"transactionId\":\"123456\"}");
		BankPaymentCurrencyConverterMessage message = topic.consume();
		assertEquals("GBP", message.getCurrency());
		assertEquals("123456", message.getTransactionId());
		assertEquals("efgh", message.getWalletAddress());
		assertEquals(200.65, message.getCurrencyAmount(), 0);
	}
	
	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testConsumeBankPaymentConfirmation() {
		get("/account/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(2000))
			.body("pending", is(false));	
		
		BankPaymentTopicProducer.send("{\"walletAddress\":\"efgh\",\"amount\":200.65,\"transactionId\":\"123456\"}");
		topic.consume();
		
		get("/account/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(2000))
			.body("pending", is(true));		
		
		BankPaymentConfirmationTopicProducer.send("{\"status\":\"SUCCESS\",\"signature\":\"sjsjsjsjs\",\"transactionId\":\"123456\"}");
		
		get("/account/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(2000))
			.body("pending", is(false));			
	}

}
