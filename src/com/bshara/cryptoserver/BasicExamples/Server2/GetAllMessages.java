package com.bshara.cryptoserver.BasicExamples.Server2;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.bshara.cryptoserver.Utils.JSONUtil;
import com.bshara.cryptoserver.Utils.Keys;
import com.bshara.cryptoserver.Utils.Entities.StatusedMessage;
import com.bshara.cryptoserver.Utils.Keys.Key;
import com.bshara.cryptoserver.Utils.RSA_AES;

public class GetAllMessages {
	public static String root = "http://localhost:8080/JRA2/main/";

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		String generateUrl = root + "allmessages/";
		
		StatusedMessage response = ((StatusedMessage) JSONUtil.postToUrl(generateUrl,
				StatusedMessage.class));
		
		ArrayList<String> cmsgs = JSON.parseObject(response.getMessage(), ArrayList.class);
		ArrayList<TreeMap<String, String>> msgs = new ArrayList<TreeMap<String, String>>();
		
		for (String c : cmsgs) {
			c = new String(Base64.getUrlDecoder().decode(c));

			System.out.println(c);
			msgs.add(decrypt(c));
		}
		
		
		
//		String str = new String(Base64.getUrlDecoder().decode(encryptedMessage));
//		TreeMap<String, Object> signedEncryptedMessage = JSON.parseObject(str, TreeMap.class);
		
		System.out.println(msgs.toString());
	}
	
	
	public static TreeMap<String, String> decrypt(String c) throws NoSuchAlgorithmException {
		
		Key k = Keys.INSTANCE.getKey(0);
		int myD = k.d;
		int myN = k.n;
		int e = k.e;
		int n = k.n;

		return RSA_AES.Decrypt(c, myD, myN, e, n);
		
	}
	
}
