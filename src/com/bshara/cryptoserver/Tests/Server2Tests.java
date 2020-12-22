package com.bshara.cryptoserver.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.TreeMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.bshara.cryptoserver.Utils.JSONUtil;
import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.RSA_AES;
import com.bshara.cryptoserver.Utils.Entities.CryptoMessager;
import com.bshara.cryptoserver.Utils.Entities.DummyKeys;
import com.bshara.cryptoserver.Utils.Entities.MessageBuilder;
import com.bshara.cryptoserver.Utils.Entities.StatusedMessage;
import com.bshara.cryptoserver.Utils.Keys.Key;

class Server2Tests {
	public static String root = "http://localhost:8080/JRA2/main/";
	private final static String id = "123456";
	private final static String password = "3456";
	private static final String OK = "OK";
	private static final String ERROR = "Error";
	private static final Logger log = Logger.getLogger(Server2Tests.class);
	static {
		BasicConfigurator.configure();
	}
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void sendMessage() throws IOException, NoSuchAlgorithmException {
		String username = "bshara";

		
		Key k = Keys.INSTANCE.getKey(0);
		String m = "hello";
		int myD = k.d;
		int myN = k.n;
		int e = k.e;
		int n = k.n;
		
		String c = RSA_AES.Encrypt(m, myD, myN, e, n);
		c = new String(Base64.getUrlEncoder().encode(c.getBytes()));
		
		
		String generateUrl = root + "chatx/" + username + "?msg=" + c;
		
		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl,
				StatusedMessage.class));
		
		
		assertTrue(true);
	}
	
	@Test
	void getAllMessages() throws IOException, NoSuchAlgorithmException {
		
		
		String generateUrl = root + "allmessages/";
		
		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl,
				StatusedMessage.class));
		
		System.out.println(response);
		
		assertTrue(true);
	}

}
