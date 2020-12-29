package com.bshara.cryptoserver.BasicExamples;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.bshara.cryptoserver.Utils.Entities.Base64;
import com.bshara.cryptoserver.Utils.Entities.ICE;
import com.bshara.cryptoserver.Utils.Entities.IceKey;
import com.bshara.cryptoserver.Utils.Entities.SecureRandomUtil;

public class ICEExample {

	public static void main(String[] args) throws UnsupportedEncodingException {

		String m, k, c;
		boolean isEqual;

		m = "Hello there x";
		m = "Hello there x";

		k = SecureRandomUtil.getRandom(8);

		c = ICE.Encrypt(m, k);

		String dec = ICE.Decrypt(c, k);

		System.out.println(dec);

		isEqual = m.equals(dec);

		System.out.println(isEqual);
	}

	static boolean ice(String m, String key) throws UnsupportedEncodingException {

		String c = ICE.Encrypt(m, key);
		return m.equals(ICE.Decrypt(c, key));
	}

	static String enc(String m, String k) {

		IceKey a = new IceKey(1);
		a.clear();
		a.set(k.getBytes(StandardCharsets.ISO_8859_1));

		byte[] ciphertext = new byte[8];
		a.encrypt(m.getBytes(StandardCharsets.ISO_8859_1), ciphertext);

		String c = new String(ciphertext, StandardCharsets.ISO_8859_1);

		return c;

	}

	static String dec(String c, String k) {

		IceKey a = new IceKey(1);
		a.clear();
		a.set(k.getBytes(StandardCharsets.ISO_8859_1));

		byte[] plaintext = new byte[8];
		a.decrypt(c.getBytes(StandardCharsets.ISO_8859_1), plaintext);

		String m2 = new String(plaintext, StandardCharsets.ISO_8859_1);

		return m2;
	}

	static boolean encDec(String m, String k) {

		IceKey a = new IceKey(1);
		a.clear();
		a.set(k.getBytes());

		byte[] ciphertext = new byte[8];
		byte[] plaintext = new byte[8];
		a.encrypt(m.getBytes(), ciphertext);

		String c2 = new String(ciphertext, StandardCharsets.ISO_8859_1);
		ciphertext = c2.getBytes(StandardCharsets.ISO_8859_1);

		a.decrypt(ciphertext, plaintext);

		// a.decrypt(ciphertext, plaintext);

		String c = new String(ciphertext);
		String m2 = new String(plaintext);

		return m.equals(m2);
	}

}
