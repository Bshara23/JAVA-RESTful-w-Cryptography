package com.bshara.cryptoserver.Tests.Algorithms;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Keys.Key;
import com.bshara.cryptoserver.Utils.Entities.RSAAlg;

class RSATests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void DecryptEncrypt() throws IOException {
		String m = "hello this is abc";
		Key k = Keys.INSTANCE.getKey(0);
		
		
		String c = RSAAlg.encrypt(m, k.e, k.n);

		
		String res = RSAAlg.decrypt(c, k.d, k.n);
		
		
		assertEquals(m, res);
		
	}
	
	@Test
	void SignVerify() throws IOException, NoSuchAlgorithmException {
		String m = "hello this is abc";
		Key k = Keys.INSTANCE.getKey(0);
		
		
		String signature = RSAAlg.sign(m, k.d, k.n);

		
		boolean res = RSAAlg.checkSign(m, signature, k.e, k.n);
		
		
		assertTrue(res);
		
	}

}
