package com.bshara.cryptoserver.Utils.Entities;

public class MyMath {
	// Function to return GCD of a and b
	public static long gcd(long a, long b) {
		if (a == 0)
			return b;
		return gcd(b % a, a);
	}

	// A simple method to evaluate
	// Euler Totient Function
	public static long phi(long n) {

		// Initialize result as n
		long result = n;

		// Consider all prime factors
		// of n and subtract their
		// multiples from result
		for (int p = 2; p * p <= n; ++p) {

			// Check if p is
			// a prime factor.
			if (n % p == 0) {

				// If yes, then update
				// n and result
				while (n % p == 0)
					n /= p;
				result -= result / p;
			}
		}

		// If n has a prime factor
		// greater than sqrt(n)
		// (There can be at-most
		// one such prime factor)
		if (n > 1)
			result -= result / n;
		return result;

	}

	public static boolean isPrime(long num) {

		int i = 2;
		boolean flag = false;
		while (i <= num / 2) {
			if (num % i == 0) {
				flag = true;
				break;
			}

			++i;
		}

		return !flag;

	}
	
	public static long findCoprimeAndSmaller(long num) {
		long x = num - 1;
		while(x > 0) {
			
			if(gcd(num, x) == 1) {
				return x;
			}
			
			x--;
		}
		return 1;
	}
	
	 public static long linearCongruence(long a, long b, long m) {
	        if(b % gcd(a, m) != 0)
	            return - 1;

	        long result = (b / gcd(a, m)) * extendedEuclid(a, m);
	        if(result < 0)
	            result += m;
	        return result;
	    }

	    private static long extendedEuclid(long a, long b) {
	        long x = 0, y = 1, lastX = 1, lastY = 0, temp;

	        if (a < b) {
	            temp = a;
	            a = b;
	            b = temp;
	        }

	        while (b != 0) {
	            long q = a / b, r = a % b;

	            a = b;
	            b = r;

	            temp = x;
	            x = lastX - q * x;
	            lastX = temp;

	            temp = y;
	            y = lastY - q * y;
	            lastY = temp;
	        }
	        return lastY;
	    }

}
