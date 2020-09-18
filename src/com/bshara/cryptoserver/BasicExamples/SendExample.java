package com.bshara.cryptoserver.BasicExamples;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;

import com.bshara.cryptoserver.Entities.SignedMessage;
import com.bshara.cryptoserver.Entities.WebMessage;
import com.bshara.cryptoserver.Entities.WebMessageWithKey;
import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;

public class SendExample {
	public static String root = "http://localhost:8080/JRA2/main/";
	private static final String id = "12345";
	private static final String password = "2345";

	public static void main(String[] args) throws Exception {
		
		KeyPair kp = CryptoUtil.generateKeyPair(2048);
		String encodePublicKey = Base64.getUrlEncoder().encodeToString(kp.getPublic().getEncoded());
		//root = "https://bscrypto.herokuapp.com/main/";

		String generateUrl = root + "connectionRequest/" + id + "?pass=" + password + "&publickey=" + encodePublicKey;
		
		System.out.println(generateUrl);
		
		WebMessageWithKey webMsgWithKey = ((WebMessageWithKey) JSONUtil.postToUrl(generateUrl, WebMessageWithKey.class));
		
		
		/*
		
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
		
		System.out.println(signedMessage.getContent());
		*/
	}
}
