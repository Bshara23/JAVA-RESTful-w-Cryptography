package com.bshara.cryptoserver.Utils.Entities;



import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RSAAlg {

	
	private static String getHash(String m) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

		messageDigest.update(m .getBytes());
		String stringHash = new String(messageDigest.digest());
		return new String(Base64.encodeBase64(stringHash.getBytes()));
	}
	
	public static String sign(String m, int d, int n) throws IOException, NoSuchAlgorithmException {
		
		String hashed = getHash(m);
		return encrypt(hashed, d, n);
	}
	
	
	public static boolean checkSign(String m, String signature, int e, int n) throws NoSuchAlgorithmException {
		
		String v1 = getHash(m);
		String v2 = decrypt(signature, e, n);
		
		return v1.equals(v2);
	}
	
	public static String decrypt(String encryptedString, int d, int n) {
		byte[] bbb = Base64.decode2(encryptedString);
		int[] ccc = bytesToInts(bbb);
		
		int[] decryptedText = new int[ccc.length];
		int i = 0;
		for (int ch : ccc) {
			// using private key to encrypt
			int decryptedChar = power(ch, d, n);
			decryptedText[i++] = decryptedChar;
		}

		String res = "";

		for (int j : decryptedText) {
			res += (char) (j % 128);
		}
		return res;
	}
	
	public static String encrypt(String plainText, int e, int n) throws IOException {
		int[] plaintextBytes = changeToInts(plainText.toCharArray());
		int[] encryptedText = new int[plaintextBytes.length];
		int i = 0;
		for (int ch : plaintextBytes) {
			// using public key to encrypt
			int encryptedChar = power(ch, e, n);
			encryptedText[i++] = encryptedChar;
		}
		byte[] aaa = intsToBytes(encryptedText);
		String s = new String(Base64.encodeBase64(aaa));
		return s;
	}
	
	
	private static int[] bytesToInts(byte buf[]) {
		   int intArr[] = new int[buf.length / 4];
		   int offset = 0;
		   for(int i = 0; i < intArr.length; i++) {
		      intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) |
		                  ((buf[1 + offset] & 0xFF) << 16) | ((buf[0 + offset] & 0xFF) << 24);  
		   offset += 4;
		   }
		   return intArr;
		}

	private static byte[] intsToBytes(int[] values) throws IOException
	{
	   ByteArrayOutputStream baos = new ByteArrayOutputStream();
	   DataOutputStream dos = new DataOutputStream(baos);
	   for(int i=0; i < values.length; ++i)
	   {
	        dos.writeInt(values[i]);
	   }

	   return baos.toByteArray();
	}

	
	
	private static int[] changeToInts(char[] chars) {
		if (chars != null && chars.length > 0) {
			int[] result = new int[chars.length];
			for (int i = 0; i < chars.length; i++) {
				result[i] = chars[i];
			}
			return result;
		}
		return null;
	}

	private static int power(int x, int y, int p) {
		// Initialize result
		int res = 1;

		// Update x if it is more
		// than or equal to p
		x = x % p;

		if (x == 0)
			return 0; // In case x is divisible by p;

		while (y > 0) {
			// If y is odd, multiply x
			// with result
			if ((y & 1) == 1)
				res = (res * x) % p;

			// y must be even now
			// y = y / 2
			y = y >> 1;
			x = (x * x) % p;
		}
		return res;
	}

	
  
}