package com.bshara.cryptoserver.BasicExamples;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Entities.RSAAlg;
import com.bshara.cryptoserver.Utils.Keys.Key;

public class RSASignVerfiyExample {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		// Key k1 = Keys.INSTANCE.getKey(0);

		String m = "abcdefg";
		Key k = Keys.INSTANCE.getKey(0);

		String sign = RSAAlg.sign(m, k.d, k.n);

		System.out.println(sign.length());

		boolean res = RSAAlg.checkSign(m, sign, k.e, k.n);

		System.out.println(res);
	}
}
