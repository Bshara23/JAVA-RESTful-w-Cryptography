package com.bshara.cryptoserver.BasicExamples.Server2;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Keys.Key;
import com.bshara.cryptoserver.Utils.RSA_AES;

public class SendMessage {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		Key k = Keys.INSTANCE.getKey(0);
		String m = "hello";
		int myD = k.d;
		int myN = k.n;
		int e = k.e;
		int n = k.n;
		
		String c = RSA_AES.Encrypt(m, myD, myN, e, n);
		c = new String(Base64.getUrlEncoder().encode(c.getBytes()));

		System.out.println(c);
		
	}
}
