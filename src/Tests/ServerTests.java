package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entities.Key;
import Entities.Message;
import Utils.ClientUtil;
import Utils.CryptoUtil;
import Utils.JSONUtil;

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
		keys.add(ClientUtil.generateKey());
		keys.add(ClientUtil.generateKey());
		keys.add(ClientUtil.generateKey());
		keys.add(ClientUtil.generateKey());
		
		String getAllKeysUrl = ClientUtil.root;
		
		ArrayList<String> fetchedkeys = JSONUtil.getArrayList(getAllKeysUrl, String.class);

		assertEquals(fetchedkeys, keys);


	}


	@Test
	void encryptDecryptMessageTest() throws IOException {
		
		String keyId = ClientUtil.generateKey();
		String data = "this is an-example-message-for-tests";
		
		String encryptedData = ClientUtil.encrypt(keyId, data);

		String decryptedData = ClientUtil.decrypt(keyId, encryptedData);

		assertEquals(data, decryptedData);
		
	}

	@Test
	void generateKeyTest() throws IOException {
		String keyId = ClientUtil.generateKey();
		
		String getAllKeysUrl = ClientUtil.root;
		ArrayList<String> fetchedkeys = JSONUtil.getArrayList(getAllKeysUrl, String.class);

		
		assertTrue(fetchedkeys.contains(keyId) && fetchedkeys.size()==1);
	}

	@Test
	void signAndVerifyTest() throws Exception {
		
		String keyId = ClientUtil.generateKey();
		String data = "this is an example message for tests";

		String signature = ClientUtil.sign(data, keyId);

		boolean isCorrect = ClientUtil.verify(data, signature, keyId);
		
		assertTrue(isCorrect);
	}


	
	
}
