package com.bshara.cryptoserver.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.TreeMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.bshara.cryptoserver.Entities.SignedMessage;
import com.bshara.cryptoserver.Entities.WebMessage;
import com.bshara.cryptoserver.Entities.WebMessageWithKey;

import com.bshara.cryptoserver.Utils.ClientUtil;
import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;
import com.bshara.cryptoserver.Utils.Entities.CryptoMessager;
import com.bshara.cryptoserver.Utils.Entities.DummyKeys;
import com.bshara.cryptoserver.Utils.Entities.MessageBuilder;
import com.bshara.cryptoserver.Utils.Entities.StatusedMessage;

class ServerTests {
	public static String root = "http://localhost:8080/JRA2/main/";
	private final static String id = "123456";
	private final static String password = "3456";
	private static final String OK = "OK";
	private static final String ERROR = "Error";
	private static final Logger log = Logger.getLogger(ServerTests.class);
	static {
		BasicConfigurator.configure();
	}

	@BeforeEach
	void setUp() throws Exception {
		// root = "https://bscrypto.herokuapp.com/main/";
	}

	@AfterEach
	void tearDown() throws Exception {
		// String clearUrl = ClientUtil.root + "clear";
		// JSONUtil.postToUrl(clearUrl, WebMessage.class);
	}

	@Test
	void loginPasswordCorrect() throws Exception {


		String username = "bshara";
		String password = "123";
		
		TreeMap<String, Object> message = MessageBuilder.createMessage().add("password", password).build();
		TreeMap<String, Object> encryptedMsg = CryptoMessager.Encrypt(message, DummyKeys.clientPrivateKey, DummyKeys.serverPublicKey);
		String uriFriendlyFormat = Base64.getUrlEncoder().encodeToString(JSON.toJSONString(encryptedMsg).getBytes());
		
		
		String generateUrl = root + "login/" + username + "?msg=" + uriFriendlyFormat;
		
		

		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl,
				StatusedMessage.class));

		assertTrue(response.getStatus().equals(OK));
	}
	@Test
	void loginPasswordWrong() throws Exception {


		String username = "bshara";
		String password = "124";
		
		TreeMap<String, Object> message = MessageBuilder.createMessage().add("password", password).build();
		TreeMap<String, Object> encryptedMsg = CryptoMessager.Encrypt(message, DummyKeys.clientPrivateKey, DummyKeys.serverPublicKey);
		String uriFriendlyFormat = Base64.getUrlEncoder().encodeToString(JSON.toJSONString(encryptedMsg).getBytes());
		
		
		String generateUrl = root + "login/" + username + "?msg=" + uriFriendlyFormat;
		
		

		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl,
				StatusedMessage.class));

		assertTrue(response.getStatus().equals(ERROR));
	}
	@Test
	void loginUsernameWrong() throws Exception {


		String username = "bshara2";
		String password = "123";
		
		TreeMap<String, Object> message = MessageBuilder.createMessage().add("password", password).build();
		TreeMap<String, Object> encryptedMsg = CryptoMessager.Encrypt(message, DummyKeys.clientPrivateKey, DummyKeys.serverPublicKey);
		String uriFriendlyFormat = Base64.getUrlEncoder().encodeToString(JSON.toJSONString(encryptedMsg).getBytes());
		
		
		String generateUrl = root + "login/" + username + "?msg=" + uriFriendlyFormat;
		
		

		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl,
				StatusedMessage.class));

		assertTrue(response.getStatus().equals(ERROR));
	}
	
	@Test
	void chatGetSameMessage() throws Exception {


		String username = "bshara";
		String password = "123";
		String myMessage = "this is bshara";
		
		TreeMap<String, Object> message = MessageBuilder.createMessage().add("password", password).add("message", myMessage).build();
		TreeMap<String, Object> encryptedMsg = CryptoMessager.Encrypt(message, DummyKeys.clientPrivateKey, DummyKeys.serverPublicKey);
		String uriFriendlyFormat = Base64.getUrlEncoder().encodeToString(JSON.toJSONString(encryptedMsg).getBytes());
		
		
		String generateUrl = root + "chat/" + username + "?msg=" + uriFriendlyFormat;
		
		
		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl,
				StatusedMessage.class));

		if(!response.getStatus().equals(OK)) {
			assertTrue(false);
		}

		String encryptedMessage = response.getMessage();
		String str = new String(Base64.getUrlDecoder().decode(encryptedMessage));
		TreeMap<String, Object> signedEncryptedMessage = JSON.parseObject(str, TreeMap.class);
		TreeMap<String, Object> decryptedMessage = CryptoMessager.Decrypt(signedEncryptedMessage, DummyKeys.clientPrivateKey, DummyKeys.serverPublicKey);
		
		String actualMessage = (String) decryptedMessage.get("message");
		
		log.info("DDDD" + actualMessage);
		assertTrue(actualMessage.equals("server says:" + myMessage));
	}
	
	
	@Test
	void connectionRequestOK() throws Exception {

		KeyPair kp = CryptoUtil.generateKeyPair(2048);
		String encodePublicKey = Base64.getUrlEncoder().encodeToString(kp.getPublic().getEncoded());
		String generateUrl = root + "connectionRequest/" + id + "?pass=" + password + "&publickey=" + encodePublicKey;

		WebMessageWithKey webMsgWithKey = ((WebMessageWithKey) JSONUtil.postToUrl(generateUrl,
				WebMessageWithKey.class));

		assertTrue(webMsgWithKey.getContent().equals(OK));

	}

	@Test
	void send() throws Exception {

		KeyPair kp = CryptoUtil.generateKeyPair(2048);
		// Send your public key to the server to communicate
		String encodePublicKey = Base64.getUrlEncoder().encodeToString(kp.getPublic().getEncoded());
		String generateUrl = root + "connectionRequest/" + id + "?pass=" + password + "&publickey=" + encodePublicKey;

		WebMessageWithKey webMsgWithKey = ((WebMessageWithKey) JSONUtil.postToUrl(generateUrl,
				WebMessageWithKey.class));

		// Get the server's public key after signing in
		PublicKey serverPublicKey = CryptoUtil.getKeyFromEncodedBase64UrlRSA(webMsgWithKey.getPublicKey());

		String plainText = "This is a test message";
		String message = CryptoUtil.signAndEncrypt(plainText, kp.getPrivate(), serverPublicKey);

		generateUrl = root + "send/" + id + "?message=" + message;

		WebMessage webMsg = ((WebMessage) JSONUtil.postToUrl(generateUrl, WebMessage.class));

		SignedMessage signedMessage = CryptoUtil.decryptAndVerify(webMsg.getContent(), kp.getPrivate(),
				serverPublicKey);

		assertTrue(signedMessage.isVerified() && signedMessage.getContent().equals(plainText));

	}

	/*
	 * @Test void listAllKeysTest() throws IOException {
	 * 
	 * ArrayList<String> keys = new ArrayList<String>(); // Generate 4 keys
	 * keys.add(ClientUtil.GET_generateKey());
	 * keys.add(ClientUtil.GET_generateKey());
	 * keys.add(ClientUtil.GET_generateKey());
	 * keys.add(ClientUtil.GET_generateKey());
	 * 
	 * String getAllKeysUrl = ClientUtil.root;
	 * 
	 * ArrayList<String> fetchedkeys = JSONUtil.getArrayList(getAllKeysUrl,
	 * String.class);
	 * 
	 * assertEquals(fetchedkeys, keys);
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @Test void encryptDecryptMessageTest() throws IOException {
	 * 
	 * String keyId = ClientUtil.GET_generateKey(); String data =
	 * "this is an-example-message-for-tests";
	 * 
	 * String encryptedData = ClientUtil.POST_encrypt(keyId, data);
	 * 
	 * String decryptedData = ClientUtil.POST_decrypt(keyId, encryptedData);
	 * 
	 * assertEquals(data, decryptedData);
	 * 
	 * }
	 * 
	 * @Test void generateKeyTest() throws IOException { String keyId =
	 * ClientUtil.GET_generateKey();
	 * 
	 * String getAllKeysUrl = ClientUtil.root; ArrayList<String> fetchedkeys =
	 * JSONUtil.getArrayList(getAllKeysUrl, String.class);
	 * 
	 * 
	 * assertTrue(fetchedkeys.contains(keyId) && fetchedkeys.size()==1); }
	 * 
	 * @Test void signAndVerifyTest() throws Exception {
	 * 
	 * String keyId = ClientUtil.GET_generateKey(); String data =
	 * "this is an example message for tests";
	 * 
	 * String signature = ClientUtil.POST_sign(data, keyId);
	 * 
	 * boolean isCorrect = ClientUtil.POST_verify(data, signature, keyId);
	 * 
	 * assertTrue(isCorrect); }
	 * 
	 */

}
