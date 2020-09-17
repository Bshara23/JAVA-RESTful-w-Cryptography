package com.bshara.cryptoserver.Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.bshara.cryptoserver.Entities.WebMessage;

public class ClientUtil {

	public final static String root = "http://localhost:8080/JRA2/main/";

	public static String GET_generateKey() throws IOException {
		String generateUrl = root + "generate?size=2048";
		String keyId = ((WebMessage) JSONUtil.postToUrl(generateUrl, WebMessage.class)).getContent();
		return keyId;
	}

	public static String POST_encrypt(String keyId, String data) throws IOException {

		data = encode(data);
		String encryptUrl = root + "encrypt/" + keyId + "?data=" + data;
		String encryptedData = ((WebMessage) JSONUtil.postToUrl(encryptUrl, WebMessage.class)).getContent();
		return encryptedData;
	}

	public static String POST_decrypt(String keyId, String encryptedData) throws IOException {

		String decryptUrl = root + "decrypt/" + keyId + "?encryptedData=" + encryptedData;
		String decryptedData = ((WebMessage) JSONUtil.postToUrl(decryptUrl, WebMessage.class)).getContent();
		return decryptedData;
	}

	public static String POST_sign(String data, String keyId) throws IOException {
		
		data = encode(data);

		String signUrl = root + "sign/" + keyId + "?data=" + data;
		String signature = ((WebMessage) JSONUtil.postToUrl(signUrl, WebMessage.class)).getContent();
		return signature;
	}

	public static boolean POST_verify(String data, String signature, String keyId) throws IOException {
		data = encode(data);

		String verifyUrl = root + "verify/" + keyId + "?data=" + data + "&signature=" + signature;
		String verifyResult = ((WebMessage) JSONUtil.postToUrl(verifyUrl, WebMessage.class)).getContent();
		return verifyResult.equals("true");
	}

	public static String encode(String str) {
		try {

			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String decode(String str) {
		try {

			return URLDecoder.decode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

}
