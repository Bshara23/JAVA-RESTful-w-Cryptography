package com.bshara.cryptoserver.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bshara.cryptoserver.Entities.Key;
import com.bshara.cryptoserver.Entities.Message;
import com.bshara.cryptoserver.Utils.ClientUtil;
import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;

class ServerTests {


	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {
		String clearUrl = ClientUtil.root + "clear";
		JSONUtil.postToUrl(clearUrl, Message.class);
	}


	@Test
	void listAllKeysTest() throws IOException {

		ArrayList<String> keys = new ArrayList<String>();
		// Generate 4 keys
		keys.add(ClientUtil.GET_generateKey());
		keys.add(ClientUtil.GET_generateKey());
		keys.add(ClientUtil.GET_generateKey());
		keys.add(ClientUtil.GET_generateKey());
		
		String getAllKeysUrl = ClientUtil.root;
		
		ArrayList<String> fetchedkeys = JSONUtil.getArrayList(getAllKeysUrl, String.class);

		assertEquals(fetchedkeys, keys);


	}


	@Test
	void encryptDecryptMessageTest() throws IOException {
		
		String keyId = ClientUtil.GET_generateKey();
		String data = "this is an-example-message-for-tests";
		
		String encryptedData = ClientUtil.POST_encrypt(keyId, data);

		String decryptedData = ClientUtil.POST_decrypt(keyId, encryptedData);

		assertEquals(data, decryptedData);
		
	}

	@Test
	void generateKeyTest() throws IOException {
		String keyId = ClientUtil.GET_generateKey();
		
		String getAllKeysUrl = ClientUtil.root;
		ArrayList<String> fetchedkeys = JSONUtil.getArrayList(getAllKeysUrl, String.class);

		
		assertTrue(fetchedkeys.contains(keyId) && fetchedkeys.size()==1);
	}

	@Test
	void signAndVerifyTest() throws Exception {
		
		String keyId = ClientUtil.GET_generateKey();
		String data = "this is an example message for tests";

		String signature = ClientUtil.POST_sign(data, keyId);

		boolean isCorrect = ClientUtil.POST_verify(data, signature, keyId);
		
		assertTrue(isCorrect);
	}


	
	
}
