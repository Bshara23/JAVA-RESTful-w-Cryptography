package com.bshara.cryptoserver.BasicExamples;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.MathUtil;
import com.bshara.cryptoserver.Utils.Entities.Base64;
import com.bshara.cryptoserver.Utils.Entities.RSAAlg;
import com.bshara.cryptoserver.Utils.Keys.Key;

public class RsaSimpleExample {

	public static void main(String[] args) throws IOException {

		// Key k1 = Keys.INSTANCE.getKey(0);

		String txt = "abcdefg";
		Key k = Keys.INSTANCE.getKey(0);

		

		
		String enc = RSAAlg.encrypt(txt, k.e, k.n);

		System.out.println(enc);

		
		

		String dec = RSAAlg.decrypt(enc, k.d, k.n);

		

		System.out.println(dec);

	}
	

}
