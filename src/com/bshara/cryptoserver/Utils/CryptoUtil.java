package com.bshara.cryptoserver.Utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Base64;

import javax.crypto.Cipher;

// for reference i used this:
// https://niels.nu/blog/2016/java-rsa.html

public class CryptoUtil {
	// TODO verify this...
	private static final String UTF_8 = "ISO-8859-1";

	public static KeyPair generateKeyPair(int keySize) throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(keySize, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();

		return pair;
	}

	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

		return Base64.getUrlEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getUrlDecoder().decode(encryptedData);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), UTF_8);
	}
	
	
	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
	    Signature privateSignature = Signature.getInstance("SHA256withRSA");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(plainText.getBytes(UTF_8));

	    byte[] signature = privateSignature.sign();

	    return Base64.getUrlEncoder().encodeToString(signature);
	}
	
	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
	    Signature publicSignature = Signature.getInstance("SHA256withRSA");
	    publicSignature.initVerify(publicKey);
	    publicSignature.update(plainText.getBytes(UTF_8));

	    byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);

	    return publicSignature.verify(signatureBytes);
	}
}
