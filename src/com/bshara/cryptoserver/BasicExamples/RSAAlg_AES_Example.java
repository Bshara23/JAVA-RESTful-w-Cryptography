package com.bshara.cryptoserver.BasicExamples;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Entities.AES;
import com.bshara.cryptoserver.Utils.Entities.ConvertUtils;
import com.bshara.cryptoserver.Utils.Entities.RSAAlg;
import com.bshara.cryptoserver.Utils.Entities.SecureRandomUtil;
import com.bshara.cryptoserver.Utils.Keys.Key;

public class RSAAlg_AES_Example {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
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
		System.out.println(sm);

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
		System.out.println(aessm);

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
		
		String m2 = sm.get("m");
		String sign2 = sm.get("sign");
		
		boolean isVerified = RSAAlg.checkSign(m2, sign2, e, n);
		
		System.out.println(isVerified);
		System.out.println(m2);
		
	}
}
