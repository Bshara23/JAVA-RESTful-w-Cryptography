package com.bshara.cryptoserver.BasicExamples;

import java.security.KeyPair;
import java.util.Base64;

import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;

public class KeyJSONParsingExample {

	public static void main(String[] args) throws Exception {
		KeyPair pair = CryptoUtil.generateKeyPair(2048);
		String res = JSONUtil.toJSONString(Base64.getUrlEncoder().encodeToString(pair.getPublic().getEncoded()));
	
		
		
		
		System.out.println(res);

	}
}
