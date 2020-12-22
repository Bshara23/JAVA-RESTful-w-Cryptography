package com.bshara.cryptoserver.BasicExamples;

import java.math.BigInteger;

public class BigIntegerMod {

	
	public static void main(String[] args) {
		// create 3 BigInteger objects
	      BigInteger bi1, bi2, bi3;
	     
	      
	      long a = 2323L;
	      
	      System.out.println(String.valueOf(a));
	      // create a BigInteger exponent
	      BigInteger exponent = new BigInteger("2");
	      bi1 = new BigInteger("7");
	      bi2 = new BigInteger("20");
	     
	      // perform modPow operation on bi1 using bi2 and exp
	      bi3 = bi1.modPow(exponent, bi2);
	      String str = bi1 + "^" +exponent+ " mod " + bi2 + " is " +bi3;
	     
	      // print bi3 value
	      System.out.println( str );

	}
}
