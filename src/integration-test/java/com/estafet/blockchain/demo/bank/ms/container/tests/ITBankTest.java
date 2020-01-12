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

import com.estafet.blockchain.demo.messages.lib.wallet.NewWalletMessage;
import com.estafet.microservices.scrum.lib.commons.properties.PropertyUtils;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ITBankTest {

	//NewWalletTopicConsumer topic = new NewWalletTopicConsumer();
	
	@Before
	public void before() {
		RestAssured.baseURI = PropertyUtils.instance().getProperty("BANK_MS_SERVICE_URI");
	}

	@After
	public void after() {
		//topic.closeConnection();
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testGetAccount() {
		get("/account/1").then()
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
			.when()
				.post("/account/2/credit/7800.67")
			.then()
				.statusCode(HttpURLConnection.HTTP_OK)
				.body("id", is(2))
				.body("balance", is(13200.67f));
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testGetSprintDays() {
		get("/sprint/1000/days").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body(is("[\"2017-10-02 00:00:00\",\"2017-10-03 00:00:00\",\"2017-10-04 00:00:00\",\"2017-10-05 00:00:00\",\"2017-10-06 00:00:00\"]"));
	}
	
	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testGetSprintDay() {
		get("/sprint/1000/day").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body(is("2017-10-02 00:00:00"));
	}

	@Test
	@DatabaseSetup("ITBankTest-data.xml")
	public void testCalculateSprints() {
		given().contentType(ContentType.JSON)
			.body("{\r\n" + 
					"	\"projectId\": 22,\r\n" + 
					"	\"noDays\": 3,\r\n" + 
					"	\"noSprints\": 3\r\n" + 
					"}")
		.when()
			.post("/calculate-sprints")
		.then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("startDate", hasSize(3))
			.body("endDate", hasSize(3))
			.body("number", hasItems(1, 2, 3));
	}
	
//	@Test
//	@DatabaseSetup("ITBankTest-data.xml")
//	public void testConsumeNewProject() {
//		NewProjectTopicProducer.send("{\"id\":2000,\"title\":\"My Project #1\",\"noSprints\":3,\"sprintLengthDays\":5}");
//		NewWalletMessage sprint = topic.consume();
//		assertThat(sprint.getNumber(), is(1));
//		assertThat(sprint.getStatus(), is("Active"));
//		assertThat(sprint.getProjectId(), is(2000));
//	}

}
