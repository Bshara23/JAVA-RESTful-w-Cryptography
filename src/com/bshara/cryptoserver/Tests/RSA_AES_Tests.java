package com.bshara.cryptoserver.Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Entities.AES;
import com.bshara.cryptoserver.Utils.Entities.ConvertUtils;
import com.bshara.cryptoserver.Utils.Entities.RSAAlg;
import com.bshara.cryptoserver.Utils.Entities.SecureRandomUtil;
import com.bshara.cryptoserver.Utils.Keys.Key;
import com.bshara.cryptoserver.Utils.RSA_AES;

class RSA_AES_Tests {
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void EncryptDecrypt() throws IOException, NoSuchAlgorithmException {
		String m = "hello this is abc";
		Key k = Keys.INSTANCE.getKey(0);
		int d = k.d;
		int e = k.e;
		int n = k.n;

		// make signature
		String sign = RSAAlg.sign(m, d, n);
		// combine the signature with the message -> signed message = sm
		TreeMap<String, String> sm = new TreeMap<String, String>();
		sm.put("m", m);
		sm.put("sign", sign);

		// Stringify sm
		String ssm = JSON.toJSONString(sm);

		// encrypt the signed message using AES
		String aesKey = SecureRandomUtil.getRandom(16);
		// encrypt the message along with the signature using the AES key, Encrypted
		// stringified signed message -> essm
		String essm = AES.encryptToBase64(ConvertUtils.stringToHexString(ssm), aesKey);

		// Encrypt the AES key using RSA
		String eAES_key = RSAAlg.encrypt(aesKey, e, n);

		TreeMap<String, String> aessm = new TreeMap<String, String>();
		aessm.put("key", eAES_key);
		aessm.put("encryptedMessage", essm);

		// Stringify message
		String saessm = JSON.toJSONString(aessm);

		TreeMap<String, String> aessm2 = JSON.parseObject(saessm, new TypeReference<TreeMap<String, String>>() {
		});

		String eAES_key2 = aessm2.get("key");
		String essm2 = aessm2.get("encryptedMessage");

		String aesKey2 = RSAAlg.decrypt(eAES_key2, d, n);

		String ssm2 = ConvertUtils.hexStringToString(AES.decryptFromBase64(essm2, aesKey2));

		TreeMap<String, String> sm2 = JSON.parseObject(ssm2, new TypeReference<TreeMap<String, String>>() {
		});

		String m2 = sm2.get("m");
		String sign2 = sm2.get("sign");

		boolean isVerified = RSAAlg.checkSign(m2, sign2, e, n);

		assertTrue(isVerified);

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
		String c = RSA_AES.Encrypt(m, a_d, a_n, b_e, b_n);
		
		// Bob received this message from Alice
		TreeMap<String, String> res = RSA_AES.Decrypt(c, b_d, b_n, a_e, a_n);

		String decryptedM = res.get("m");
		String isSignValid = res.get("sign");

		System.out.println(decryptedM);
		System.out.println(isSignValid);

		assertTrue(isSignValid.equals("Yes") && decryptedM.equals(m));
	}
}
