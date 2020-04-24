package com.estafet.blockchain.demo.bank.ms.container.tests;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.estafet.blockchain.demo.bank.ms.model.Account;
import com.estafet.blockchain.demo.bank.ms.model.Transaction;
import com.estafet.blockchain.demo.bank.ms.repository.AccountRepository;
import com.estafet.openshift.boost.commons.lib.properties.PropertyUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.estafet.blockchain.demo.messages.lib.bank.BankPaymentCurrencyConverterMessage;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
		properties = "classpath:integration-test.properties")
public class ITBankTest {

	CurrencyConverterConsumer topic = new CurrencyConverterConsumer();

	@Autowired
	private AccountRepository accountRepository;

	@Before
	public void before() {
		RestAssured.baseURI = PropertyUtils.instance().getProperty("BANK_MS_SERVICE_URI");

		Set<Transaction> transactionList = new HashSet<>();

		Account account = new Account();
		account.setId("1000");
		account.setAccountName("Dennis");
		account.setCurrency("USD");
		account.setWalletAddress("abcd");

		Transaction transaction = new Transaction();
		transaction.setOrder(1);
		transaction.setAmount(200);
		transaction.setStatus("CLEARED");
		transaction.setDescription("Opening Deposit");
		transaction.setWalletTransactionId("2345");

		Transaction transaction1 = new Transaction();
		transaction1.setOrder(2);
		transaction1.setAmount(-50);
		transaction1.setStatus("CLEARED");
		transaction1.setDescription("User Withdrawal");
		transaction1.setWalletTransactionId("3456");

		transactionList.add(transaction);
		transactionList.add(transaction1);
		account.setTransactions(transactionList);
		accountRepository.save(account);

		Set<Transaction> transactionList1 = new HashSet<>();

		Account account1 = new Account();
		account1.setId("2000");
		account1.setAccountName("Iryna");
		account1.setCurrency("GBP");
		account1.setWalletAddress("efgh");

		Transaction transaction2 = new Transaction();
		Transaction transaction3 = new Transaction();

		transaction2.setOrder(3);
		transaction2.setAmount(5000);
		transaction2.setStatus("CLEARED");
		transaction2.setDescription("Opening Deposit");
		transaction2.setWalletTransactionId("775655");

		transaction3.setOrder(4);
		transaction3.setAmount(400);
		transaction3.setStatus("CLEARED");
		transaction3.setDescription("User Deposit");
		transaction3.setWalletTransactionId("3432222");

		transactionList1.add(transaction2);
		transactionList1.add(transaction3);
		account1.setTransactions(transactionList1);
		accountRepository.save(account1);

	}

	@After
	public void after() {
		topic.closeConnection();
	}


	@Test
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
	}

	@Test
	public void deleteExchangeRates() {
		delete("accounts").then()
		.statusCode(HttpURLConnection.HTTP_OK)
		.body(is("Accounts Deleted"));
	}
	
	@Test
	public void testConsumeBankPayment() {
		BankPaymentTopicProducer.send("{\"walletAddress\":\"efgh\",\"amount\":200.65,\"transactionId\":\"123456\"}");
		BankPaymentCurrencyConverterMessage message = topic.consume();
		assertEquals("GBP", message.getCurrency());
		assertEquals("123456", message.getTransactionId());
		assertEquals("efgh", message.getWalletAddress());
		assertEquals(200.65, message.getCurrencyAmount(), 0);
	}
	
	@Test
	public void testConsumeBankPaymentConfirmation() {
		get("/account/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is("2000"))
			.body("pending", is(false));	
		
		BankPaymentTopicProducer.send("{\"walletAddress\":\"efgh\",\"amount\":200.65,\"transactionId\":\"123456\"}");
		topic.consume();
		
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
