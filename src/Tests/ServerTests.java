package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entities.Key;
import Entities.Message;
import Utils.ClientUtil;
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
	void basicGetTest() throws IOException {

	}

	@Test
	void listAllKeysTest() throws IOException {

		
		// Generate 4 keys
		ClientUtil.generateKey();
		ClientUtil.generateKey();
		ClientUtil.generateKey();
		ClientUtil.generateKey();
		
		String getAllKeysUrl = ClientUtil.root;
		
		ArrayList<String> keys = JSONUtil.getArrayList(getAllKeysUrl, String.class);

		assertEquals(keys.size(), 4);


	}



	@Test
	void encryptDecryptMessageTest() throws IOException {
		
		String keyId = ClientUtil.generateKey();
		String data = "this-is-an-example-message-for-tests";
		
		String encryptedData = ClientUtil.encrypt(keyId, data);

		String decryptedData = ClientUtil.decrypt(keyId, encryptedData);

		assertEquals(decryptedData, data);
		
	}

	@Test
	void generateKeyTest() throws IOException {
		String keyId = ClientUtil.generateKey();
		assertEquals(keyId, "1");
	}

	@Test
	void signTest() {

	}

	@Test
	void verifyTest() {

	}
	
	
}
