package com.bshara.cryptoserver.Utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import com.bshara.cryptoserver.Entities.SignedMessage;

// for reference i used this:
// https://niels.nu/blog/2016/java-rsa.html

public class CryptoUtil {
	// TODO verify this...
	private static final String UTF_8 = "ISO-8859-1";

	public static PublicKey getKeyFromEncodedBase64UrlRSA(String publicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		// decode string from base 64 to bytes
		byte[] publicBytes = Base64.getUrlDecoder().decode(publicKey);

		// make encoded key
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);

		
		
		// create key by the RSA algorithms
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		//pkcs8 
		
		
		return pubKey;
	}

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
	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes(UTF_8));

		byte[] signature = privateSignature.sign();

		return Base64.getUrlEncoder().encodeToString(signature);
	}
	
	public static String decrypt(String encryptedData, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getUrlDecoder().decode(encryptedData);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), UTF_8);
	}

	

	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		byte[] signatureBytes = Base64.getUrlDecoder().decode(signature);
		
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes(UTF_8));

		return publicSignature.verify(signatureBytes);
	}

	public static String signAndEncrypt(String plainText, PrivateKey myKey, PublicKey othersKey) throws Exception {
		
		String encrypted = CryptoUtil.encrypt(plainText , othersKey);
		String signed = CryptoUtil.sign(plainText, myKey);

		
		String combined = encrypted + signed;
		
		return combined;
		
		
	}

	public static SignedMessage decryptAndVerify(String combined, PrivateKey myKey, PublicKey othersKey) throws Exception {
		// now assuming that here is the server side
		String encrypted = combined.substring(0, combined.length() / 2);
		String signed = combined.substring(combined.length() / 2, combined.length());

		String decrypted = CryptoUtil.decrypt(encrypted, myKey);
		boolean isVerified = CryptoUtil.verify(decrypted, signed, othersKey);
		
		return new SignedMessage(decrypted, isVerified);
	}
}
