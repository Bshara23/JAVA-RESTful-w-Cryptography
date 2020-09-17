package com.bshara.cryptoserver.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bshara.cryptoserver.Entities.SignedMessage;
import com.bshara.cryptoserver.Entities.WebMessage;
import com.bshara.cryptoserver.Entities.WebMessageWithKey;

import com.bshara.cryptoserver.Utils.ClientUtil;
import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;

class ServerTests {
	public final static String root = "http://localhost:8080/JRA2/main/";
	private final static String id = "123456";
	private final static String password = "3456";
	private static final String OK = "OK";
	private static final String ERROR = "Error";
	@BeforeEach
	void setUp() throws Exception {

	}

	@AfterEach
	void tearDown() throws Exception {
		String clearUrl = ClientUtil.root + "clear";
		JSONUtil.postToUrl(clearUrl, WebMessage.class);
	}

	@Test
	void connectionRequestOK() throws Exception {

		KeyPair kp = CryptoUtil.generateKeyPair(2048);
		String encodePublicKey = Base64.getUrlEncoder().encodeToString(kp.getPublic().getEncoded());
		String generateUrl = root + "connectionRequest/" + id + "?pass=" + password + "&publickey=" + encodePublicKey;
		
		WebMessageWithKey webMsgWithKey = ((WebMessageWithKey) JSONUtil.postToUrl(generateUrl, WebMessageWithKey.class));

		assertTrue(webMsgWithKey.getContent().equals(OK));
		
	}

	
	@Test
	void send() throws Exception {

		KeyPair kp = CryptoUtil.generateKeyPair(2048);
		// Send your public key to the server to communicate
		String encodePublicKey = Base64.getUrlEncoder().encodeToString(kp.getPublic().getEncoded());
		String generateUrl = root + "connectionRequest/" + id + "?pass=" + password + "&publickey=" + encodePublicKey;
		
		WebMessageWithKey webMsgWithKey = ((WebMessageWithKey) JSONUtil.postToUrl(generateUrl, WebMessageWithKey.class));

		
		// Get the server's public key after signing in
		PublicKey serverPublicKey = CryptoUtil.getKeyFromEncodedBase64UrlRSA(webMsgWithKey.getPublicKey());
		
		
		
		String plainText = "This is a test message";
		String message = CryptoUtil.signAndEncrypt(plainText, kp.getPrivate(), serverPublicKey);
		
		
		
		generateUrl = root + "send/" + id + "?message=" + message;
		
		WebMessage webMsg = ((WebMessage) JSONUtil.postToUrl(generateUrl, WebMessage.class));
		
		SignedMessage signedMessage = CryptoUtil.decryptAndVerify(webMsg.getContent(), kp.getPrivate(), serverPublicKey);
		
		
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
