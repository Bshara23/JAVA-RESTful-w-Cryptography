package com.bshara.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.TreeMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bshara.cryptoserver.Utils.Entities.CryptoMessager;
import com.bshara.cryptoserver.Utils.Entities.DummyKeys;

class UtilityTests {
	private static final Logger log = Logger.getLogger(UtilityTests.class);
	static {
		BasicConfigurator.configure();
	}

	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {
	}
	@Test
	void test_encrypt_decrypt() throws Exception {
		TreeMap<String, Object> originalMessage = new TreeMap<String, Object>();

		originalMessage.put("userid", "23123123");
		originalMessage.put("phone", "152255855");
		originalMessage.put("aaa", "bbbb");

		TreeMap<String, Object> signedEncryptedMessage = CryptoMessager.Encrypt(originalMessage, DummyKeys.clientPrivateKey,
				DummyKeys.serverPublicKey);

		TreeMap<String, Object> decryptedMessage = CryptoMessager.Decrypt(signedEncryptedMessage, DummyKeys.serverPrivateKey,
				DummyKeys.clientPublicKey);

		assertEquals(originalMessage, decryptedMessage);
	}

}
