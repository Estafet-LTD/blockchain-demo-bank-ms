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
			.body("id", is(1))
			.body("walletAddress", is("abcd"))
			.body("accountName", is("Dennis"))
			.body("publicKey", is("dddd"))
			.body("currency", is("USD"))
			.body("balance",is(150.0f))
			.body("pendingBalance",is(0.0f));
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
				.body("id", is(2))
				.body("balance", is(13200.67f));
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testDedit() {
		given().contentType(ContentType.JSON)
			.body("{ \"amount\": 20.0, \"currency\": \"USD\" }")
			.when()
				.post("/account/1000/debit")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is(1))
				.body("currency", is("USD"))
				.body("balance", is(130.00f));
	}
	
	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testCreateAccount() {
		given().contentType(ContentType.JSON)
			.body("{\r\n" + 
					"    \"walletAddress\": \"abcd\",\r\n" + 
					"    \"walletName\": \"Peter\"\r\n" + 
					"}")
			.when()
				.post("/account/currency/USD")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("walletAddress", is("abcd"))
				.body("accountName", is("Peter"));
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
		BankPaymentTopicProducer.send("{\"walletAddress\":\"ssjsjaja\",\"walletName\":\"Dennis\",\"currency\":\"USD\"}");
		topic.consume();
	}

}
