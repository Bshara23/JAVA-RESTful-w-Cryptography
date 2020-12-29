package com.bshara.cryptoserver.Tests.Algorithms;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.RSA_AES;
import com.bshara.cryptoserver.Utils.RSA_ICE;
import com.bshara.cryptoserver.Utils.Keys.Key;

class RSA_ICE_Tests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void EncryptDecryptClass() throws IOException, NoSuchAlgorithmException {

		String m = "hello this is abc";
		Key k1 = Keys.INSTANCE.getKey(0);
		Key k2 = Keys.INSTANCE.getKey(1);

		// assuming that Alice is sending a message to Bob
		int a_d = k1.d;
		int a_e = k1.e;
		int a_n = k1.n;

		int b_d = k2.d;
		int b_e = k2.e;
		int b_n = k2.n;

		
		// Alice will send this to bob
		String c = RSA_ICE.Encrypt(m, a_d, a_n, b_e, b_n);
		
		// Bob received this message from Alice
		TreeMap<String, String> res = RSA_ICE.Decrypt(c, b_d, b_n, a_e, a_n);

		String decryptedM = res.get("m");
		String isSignValid = res.get("sign");

//		System.out.println(decryptedM);
//		System.out.println(isSignValid);

		assertTrue(isSignValid.equals("Yes") && decryptedM.equals(m));
	}

}
