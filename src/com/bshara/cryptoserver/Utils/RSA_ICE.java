package com.bshara.cryptoserver.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bshara.cryptoserver.Utils.Entities.ICE;
import com.bshara.cryptoserver.Utils.Entities.RSAAlg;
import com.bshara.cryptoserver.Utils.Entities.SecureRandomUtil;

public class RSA_ICE {
	/**
	 * This function takes a message, encrypt and sign it and returns a stringified
	 * TreeMap<String, String> object that holds the encrypted message along with
	 * the symmetric key to decrypted and the signature.
	 * 
	 * @param m   - a message that the sender wants to encrypt and sign.
	 * @param myD - The receiver's private key.
	 * @param myN - The receiver's modulo number.
	 * @param e   - The sender's public key.
	 * @param n   - The sender's modulo number.
	 * 
	 * @return Stringified TreeMap<String, String> object that holds two strings,
	 *         "key" that holds the symmetric key used for encryption,
	 *         "encryptedMessage" that was encrypted using the symmetric key and the
	 *         encrypted message it self holds the sender signature along with the
	 *         original message.
	 */
	public static String Encrypt(String m, int myD, int myN, int e, int n)
			throws IOException, NoSuchAlgorithmException {

		// make signature
		String sign = RSAAlg.sign(m, myD, myN);
		// combine the signature with the message -> signed message = sm
		TreeMap<String, String> sm = new TreeMap<String, String>();
		sm.put("m", m);
		sm.put("sign", sign);
		System.out.println("Sign: " + sign);
		// Stringify sm
		String ssm = JSON.toJSONString(sm);

		// encrypt the signed message using AES
		String aesKey = SecureRandomUtil.getRandom(8);
		aesKey = "12345678";
		// encrypt the message along with the signature using the AES key, Encrypted
		// stringified signed message -> essm
		System.out.println("Key1: " + aesKey);

		String essm = ICE.Encrypt(ssm, aesKey);
		System.out.println("C2: " + essm);

		// Encrypt the AES key using RSA
		String eAES_key = RSAAlg.encrypt(aesKey, e, n);

		TreeMap<String, String> aessm = new TreeMap<String, String>();
		aessm.put("key", eAES_key);
		aessm.put("encryptedMessage", essm);

		// Stringify message
		String saessm = JSON.toJSONString(aessm);

		return saessm;
	}

	/**
	 * This function takes a stringified TreeMap<String, String> object in JSON
	 * format, that holds an encrypted message and an encrypted key, the message is
	 * assumed to hold a signature.
	 * 
	 * @param c   - a stringified object TreeMap<String, String> that holds an
	 *            encrypted message and an encrypted key.
	 * @param myD - The receiver's private key.
	 * @param myN - The receiver's modulo number.
	 * @param e   - The sender's public key.
	 * @param n   - The sender's modulo number.
	 * 
	 * @return TreeMap<String, String> object that holds two strings, "sign" that
	 *         can be "Yes" or "No" depending in whether The message signature is
	 *         valid or not, "m" that hold the decrypted message.
	 * @throws UnsupportedEncodingException
	 */
	public static TreeMap<String, String> Decrypt(String c, int myD, int myN, int e, int n)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		TreeMap<String, String> aessm2 = JSON.parseObject(c, new TypeReference<TreeMap<String, String>>() {
		});

		String eAES_key2 = aessm2.get("key");
		String essm2 = aessm2.get("encryptedMessage");

		String aesKey2 = RSAAlg.decrypt(eAES_key2, myD, myN);
		aesKey2 = "12345678";
		String ssm2 = ICE.Decrypt(essm2, aesKey2);

		System.out.println("Decrypted: " + ssm2);

		TreeMap<String, String> sm2 = JSON.parseObject(ssm2, new TypeReference<TreeMap<String, String>>() {
		});

		String m2 = sm2.get("m");
		String sign2 = sm2.get("sign");
		System.out.println("Sign2: " + sign2);

		boolean isVerified = RSAAlg.checkSign(m2, sign2, e, n);
		String isSignatureValid = isVerified ? "Yes" : "No";

		TreeMap<String, String> res = new TreeMap<String, String>();
		res.put("sign", isSignatureValid);
		res.put("m", m2);

		return res;
	}
}
