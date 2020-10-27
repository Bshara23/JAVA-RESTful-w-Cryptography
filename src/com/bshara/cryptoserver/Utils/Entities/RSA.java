package com.bshara.cryptoserver.Utils.Entities;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class RSA {
	private static final Logger log = Logger.getLogger(RSA.class);
	static {
		BasicConfigurator.configure();
	}
	private static int KEYSIZE = 2048;

	public static Map<String, String> generateKeyPair() throws Exception {

		SecureRandom sr = new SecureRandom();

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

		kpg.initialize(KEYSIZE, sr);

		KeyPair kp = kpg.generateKeyPair();

		Key publicKey = kp.getPublic();
		byte[] publicKeyBytes = publicKey.getEncoded();
		String pub = new String(Base64.encodeBase64(publicKeyBytes), ConfigureEncryptAndDecrypt.CHAR_ENCODING);

		Key privateKey = kp.getPrivate();
		byte[] privateKeyBytes = privateKey.getEncoded();
		String pri = new String(Base64.encodeBase64(privateKeyBytes), ConfigureEncryptAndDecrypt.CHAR_ENCODING);

		Map<String, String> map = new HashMap<String, String>();
		map.put("publicKey", pub);
		map.put("privateKey", pri);
		RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
		BigInteger bint = rsp.getModulus();
		byte[] b = bint.toByteArray();
		byte[] deBase64Value = Base64.encodeBase64(b);
		String retValue = new String(deBase64Value);
		map.put("modulus", retValue);
		return map;
	}

	// using the public key = encryption key (d), encrypt the source (original
	// message)
	// which is basically: source = h and we are doing h^d (mod n), where h^d is the
	// encrypted message
	public static String encrypt(String source, String publicKey) throws Exception {
		Key key = getPublicKey(publicKey);

		Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// b = big int
		byte[] b = source.getBytes();
		byte[] b1 = cipher.doFinal(b);
		return new String(Base64.encodeBase64(b1), ConfigureEncryptAndDecrypt.CHAR_ENCODING);
	}

	// using the private key = decryption key (d), decrypt the cryptograph (coded
	// message)
	// which is basically: cryptograph = h^d and we are doing h^d^e = h (mod n),
	// where h is the original message
	public static String decrypt(String cryptograph, String privateKey) throws Exception {
		Key key = getPrivateKey(privateKey);

		Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());

		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	// Generate a public key = encryption key (e), which is a prime number and not
	// equal to the decryption key
	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	// Generate a private key = decryption key, which is a prime number and not
	// equal to the encryption key
	public static PrivateKey getPrivateKey(String key) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	// https://en.wikipedia.org/wiki/RSA_(cryptosystem)#Signing_messages
	// 1. create a hash value from the message => h = hash(content)
	// 2. sign the hash value => h^d (mod n) where d = decryption key = private key
	public static String sign(String content, String privateKey) {

		String charset = ConfigureEncryptAndDecrypt.CHAR_ENCODING;
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			Signature signature = Signature.getInstance("SHA256WithRSA");

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {

		}

		return null;
	}

	// sing = h^d (mod n)
	// 3. to check the sign, do: sing^e = (h^d)^e (mod n), where e = encryption key
	// = public key
	// using the exponentiation rules we get that: (h^e)^d = h^ed = h^de = (h^d)^e =
	// h (mod n)
	// 4. verify that sign^e = h (mod n)
	public static boolean checkSign(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode2(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			Signature signature = Signature.getInstance("SHA256WithRSA");

			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));

			boolean bverify = signature.verify(Base64.decode2(sign));
			return bverify;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return false;
	}

}