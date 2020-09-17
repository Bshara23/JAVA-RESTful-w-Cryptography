package com.bshara.cryptoserver.BasicExamples;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.bshara.cryptoserver.Entities.SignedMessage;
import com.bshara.cryptoserver.Utils.CryptoUtil;

public class SignThenEncrypt {
	private static final String UTF_8 = "ISO-8859-1";

	public static void main(String[] args) throws Exception {

		// The RSA key
		KeyPair kpClient = CryptoUtil.generateKeyPair(2048);
		KeyPair kpServer = CryptoUtil.generateKeyPair(2048);
		KeyPair hacker = CryptoUtil.generateKeyPair(2048);

		String plainText = "awdawdd";

		String signedEncr = CryptoUtil.signAndEncrypt(plainText, kpClient.getPrivate(), kpServer.getPublic());

		String encodePublicKey = Base64.getUrlEncoder().encodeToString(kpClient.getPublic().getEncoded());
		// get the users public key
		PublicKey clientPublicKey = CryptoUtil.getKeyFromEncodedBase64UrlRSA(encodePublicKey);
		
		
		SignedMessage signedMessage = CryptoUtil.decryptAndVerify(signedEncr, kpServer.getPrivate(), clientPublicKey);

		System.out.println(signedMessage);

	}

}
