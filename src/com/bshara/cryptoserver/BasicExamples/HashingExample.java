package com.bshara.cryptoserver.BasicExamples;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.bshara.cryptoserver.Utils.Entities.Base64;



public class HashingExample {

	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		String stringToHash = "myPassword";
		messageDigest.update(stringToHash .getBytes());
		String stringHash = new String(messageDigest.digest());
		String s = new String(Base64.encodeBase64(stringHash.getBytes()));
		System.out.println(s);
		
		
		
		
		
		
		
	}
}
