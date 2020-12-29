package com.bshara.cryptoserver.Utils.Entities;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ICE {

	private static final char CH = '#';
	private static final Charset charset = StandardCharsets.ISO_8859_1;
	
	
	static String enc(String m, String k) {

		IceKey a = new IceKey(1);
		a.clear();
		a.set(k.getBytes());

		byte[] ciphertext = new byte[8];
		a.encrypt(m.getBytes(), ciphertext);

		String c = new String(ciphertext, charset);

		return c;

	}

	static String dec(String c, String k) {

		IceKey a = new IceKey(1);
		a.clear();
		a.set(k.getBytes());

		byte[] plaintext = new byte[8];
		a.decrypt(c.getBytes(charset), plaintext);

		String m2 = new String(plaintext);

		return m2;
	}

	public static String Encrypt(String m, String key) throws UnsupportedEncodingException {

		m = Base64.encode(m);
		ArrayList<String> splits = splitStringBySize(m, 8);

		int idx = splits.size() - 1;
		splits.set(idx, fixSize(splits.get(idx)));

		String c = "";
		for (String str : splits) {
			str = replaceWhiteSpaces(str);
			c += enc(str, key);
		}

		return c;
	}

	public static String Decrypt(String c, String key) throws UnsupportedEncodingException {

		ArrayList<String> splits = splitStringBySize(c, 8);

		IceKey a = new IceKey(1);
		a.clear();

		String m = "";
		for (String str : splits) {

			if (str.length() > 0) {

				m += dec(str, key);

			}

		}

		m = clean(m);
		m = Base64.decode(m);
		return m;
	}

	private static ArrayList<String> splitStringBySize(String str, int size) {
		ArrayList<String> split = new ArrayList<>();
		for (int i = 0; i <= str.length() / size; i++) {
			split.add(str.substring(i * size, Math.min((i + 1) * size, str.length())));
		}
		return split;
	}

	private static String fixSize(String str) {

		while (str.length() < 8)
			str += CH;

		return str;
	}

	private static String clean(String str) {

		return str.replaceAll(CH + "", "");

	}

	private static String replaceWhiteSpaces(String str) {
		return str.replaceAll(" ", CH+"");
	}

	private static String returnWhiteSpaces(String str) {
		return str.replaceAll(CH+"", " ");
	}
}
