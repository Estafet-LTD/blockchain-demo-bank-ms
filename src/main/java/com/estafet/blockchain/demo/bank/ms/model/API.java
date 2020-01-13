package com.estafet.blockchain.demo.bank.ms.model;

import java.util.StringTokenizer;

public class API {

	private String version;

	public API() {
	}

	public API(String version) {
		this.version = version;
	}

	public String getVersion() {
		StringTokenizer tokenizer = new StringTokenizer(version.replaceAll("\\-SNAPSHOT", ""), ".");
		String p1 = tokenizer.nextToken();
		String p2 = tokenizer.nextToken();
		String p3 = tokenizer.nextToken();
		return p1 + "." + p2 + "." + Integer.toString(Integer.parseInt(p3) - 1);
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
