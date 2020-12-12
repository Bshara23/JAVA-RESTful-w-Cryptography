package com.bshara.cryptoserver.BasicExamples;

import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.bshara.cryptoserver.Utils.MathUtil;
import com.bshara.cryptoserver.Utils.PrimeNumbers;
import com.bshara.cryptoserver.Utils.Entities.MyMath;

//https://simple.wikipedia.org/wiki/RSA_algorithm#:~:text=RSA%20(Rivest%E2%80%93Shamir%E2%80%93Adleman,can%20be%20given%20to%20anyone.
public class MakeKeys {

	public static void main(String[] args) {
		Random random = ThreadLocalRandom.current();

		long p = PrimeNumbers.p5;
		long q = PrimeNumbers.p6;
		long n = p * q;
		long phi = (p - 1) * (q - 1);
		long e, d;

		System.out.println("k.n = " + n);
		System.out.println("k.p = " + p);
		System.out.println("k.q = " + q);
		System.out.println("k.phi = " + (p - 1) * (q - 1));

		do {
			e = Math.abs(random.nextLong()) % phi + 1;
		} while (MathUtil.gcd(e, phi) != 1);

		d = (MathUtil.linearCongruence(e, 1, phi)) % phi;

		
		System.out.println("k.e = " + e);
		System.out.println("k.d = " + d);
		
	}

}
