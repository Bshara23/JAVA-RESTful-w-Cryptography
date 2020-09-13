package com.bshara.cryptoserver.BasicExamples;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;

import com.bshara.cryptoserver.Utils.CryptoUtil;

public class EncryptionExample {

	public static void main(String[] args) throws Exception {
		
		signValidateExample();
		//encryptDecryptExample();
	}

	
	
	static void encryptDecryptExample() throws Exception {
		// First generate a public/private key pair
		KeyPair pair = CryptoUtil.generateKeyPair(2048);

		// Our secret message
		String message = "the answer to life the universe and everything";

		// Encrypt the message
		String cipherText = CryptoUtil.encrypt(message, pair.getPublic());

		// Now decrypt it
		String decipheredMessage = CryptoUtil.decrypt(cipherText, pair.getPrivate());

		System.out.println(decipheredMessage);
	}
	
	static void signValidateExample() throws Exception {
		KeyPair pair = CryptoUtil.generateKeyPair(2048);

		String signature = CryptoUtil.sign("foobar", pair.getPrivate());

		//Let's check the signature
		boolean isCorrect = CryptoUtil.verify("foobar", signature, pair.getPublic());
		System.out.println("Signature correct: " + isCorrect);
	}

}
