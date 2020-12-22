package com.bshara.cryptoserver.BasicExamples;

import java.math.BigInteger;
import java.util.Arrays;

import com.bshara.cryptoserver.Utils.Entities.IceKey;

public class myMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IceKey a = new IceKey(1);
		a.clear();
		byte[] ciphertext = new byte[8];
		byte[] plaintext2 = new byte[8];

		byte[] key = "12345678".getBytes();
		a.set(key);
		
		String plaintext = "abcdefgh";
		a.encrypt(plaintext.getBytes(), ciphertext);
		a.decrypt(ciphertext, plaintext2);
		System.out.println(new String(plaintext2));

	}


}
