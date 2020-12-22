package com.bshara.cryptoserver.Utils.Entities;

import java.util.ArrayList;

public class ICE {

	private static final char CH = 'd';

	public static String Encrypt(String m, String key) {

		ArrayList<String> splits = splitStringBySize(m, 8);

		int idx = splits.size() - 1;
		splits.set(idx, fixSize(splits.get(idx)));

		IceKey a = new IceKey(1);
		a.clear();

		String c = "";
		for (String str : splits) {
//			str = replaceWhiteSpaces(str);

			byte[] ciphertext = new byte[8];
			a.set(key.getBytes());
			a.encrypt(str.getBytes(), ciphertext);
			c += new String(ciphertext);
		}

		return c;
	}

	public static String Decrypt(String c, String key) {

		ArrayList<String> splits = splitStringBySize(c, 8);

		IceKey a = new IceKey(1);
		a.clear();

		String m = "";
		for (String str : splits) {
			byte[] plaintext2 = new byte[8];
			a.set(key.getBytes());
			if (str.length() > 0) {
				a.decrypt(str.getBytes(), plaintext2);
//				m += returnWhiteSpaces(new String(plaintext2));
				m += new String(plaintext2);

			}

		}

		return clean(m);
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

		return str;
//		return str.replaceAll(CH + "", "");

	}

//	private static String replaceWhiteSpaces(String str) {
//		return str.replaceAll(" ", "x");
//	}
//	private static String returnWhiteSpaces(String str) {
//		return str.replaceAll("x", " ");
//	}
}
