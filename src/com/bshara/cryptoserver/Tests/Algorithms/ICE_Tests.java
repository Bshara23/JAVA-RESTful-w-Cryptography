package com.bshara.cryptoserver.Tests.Algorithms;

import static org.junit.jupiter.api.Assertions.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.bshara.cryptoserver.Utils.Entities.Base64;
import com.bshara.cryptoserver.Utils.Entities.ICE;
import com.bshara.cryptoserver.Utils.Entities.SecureRandomUtil;

class ICE_Tests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	
	@Test
	void ICE_Encrypt_Decrypt_WhiteSpaces() throws UnsupportedEncodingException {
		
		String m, k, c;
		boolean isEqual;
		
		
		m = "Hello there x";

		k = SecureRandomUtil.getRandom(8);

		c = ICE.Encrypt(m, k);

		
		isEqual = m.equals(ICE.Decrypt(c, k));
		
		assertTrue(isEqual);
	}
	
	@Test
	void ICE_Encrypt_Decrypt() throws UnsupportedEncodingException {
		
		int n = 1000;
		int cnt = 0;
		String m, m2, k, c;
		boolean isEqual;
		
		for (int i = 0; i < n; i++) {
			m = SecureRandomUtil.getRandom(13);
			k = SecureRandomUtil.getRandom(8);

			c = ICE.Encrypt(m, k);
			
			//c = Base64.encode(c);
			//c = Base64.decode(c);
			
			
			
			m2 = ICE.Decrypt(c, k);
			isEqual = m.equals(m2);
			
			
			if (!isEqual) {
				cnt += 1;
			}

		}
		
		assertTrue(cnt == 0);
	}
	
	
	@Test
	void ICE_Encrypt_Decrypt_different_keys() throws UnsupportedEncodingException {
		
		int n = 1000;
		int cnt = 0;
		String m, m2, k, k2, c;
		boolean isEqual;
		
		for (int i = 0; i < n; i++) {
			m = SecureRandomUtil.getRandom(13);
			k = SecureRandomUtil.getRandom(8);
			k2 = SecureRandomUtil.getRandom(8);

			c = ICE.Encrypt(m, k);
			
			//c = Base64.encode(c);
			//c = Base64.decode(c);
			
			
			
			m2 = ICE.Decrypt(c, k2);
			isEqual = m.equals(m2);
			
			
			if (!isEqual) {
				cnt += 1;
			}

		}
		
		assertTrue(cnt != 0);
	}

}
