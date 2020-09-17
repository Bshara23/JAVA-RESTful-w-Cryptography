package com.bshara.cryptoserver.BasicExamples;

import java.awt.List;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;

import org.json.JSONException;

import com.bshara.cryptoserver.Entities.WebMessage;
import com.bshara.cryptoserver.Utils.ClientUtil;
import com.bshara.cryptoserver.Utils.CryptoUtil;
import com.bshara.cryptoserver.Utils.JSONUtil;

// http://localhost:8080/JRA2/main?a=5
public class ClientUsageExamples {

	public static void main(String[] args) throws IOException, JSONException {

		generateKeys();
		System.out.println("dawdwa");
	}




	static void generateKeys() throws IOException {

		WebMessage str;
		String url = "http://localhost:8080/JRA2/main/generate?size=2048";

		str = JSONUtil.postToUrl(url, WebMessage.class);
		System.out.println(str);

		str = JSONUtil.postToUrl(url, WebMessage.class);
		System.out.println(str);

		str = JSONUtil.postToUrl(url, WebMessage.class);
		System.out.println(str);

		// str = MyUtil.getJSONFromUrl("http://localhost:8080/JRA2/main/");
		// System.out.println(str);

	}

	static void getKeysArrayList() throws IOException {
		ArrayList<String> keys = JSONUtil.getArrayList("http://localhost:8080/JRA2/main/", String.class);
		System.out.println("number of keys=" + keys.size());

		for (int i = 0; i < keys.size(); i++) {
			System.out.println(keys.get(i));
		}

	}

	static void encryptMessage() throws IOException {
		String keyId = ClientUtil.GET_generateKey();
		String data = "this-is-an-example-message-for-tests";

		String encryptedData = ClientUtil.POST_encrypt(keyId, data);
		System.out.println(encryptedData);
		// String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
	}

	static void encryptAndDecryptMessage() throws IOException {
		String keyId = ClientUtil.GET_generateKey();
		String data = "this-is-an-example-message-for-tests";

		String encryptedData = ClientUtil.POST_encrypt(keyId, data);
		System.out.println(encryptedData);

		String decryptedData = ClientUtil.POST_decrypt(keyId, encryptedData);
		System.out.println(decryptedData);

		// String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
	}

	static void signAndVerify() throws IOException {
		String keyId = ClientUtil.GET_generateKey();
		String data = "this-is-an-example-message-for-tests";

		String signature = ClientUtil.POST_sign(data, keyId);

		boolean isCorrect = ClientUtil.POST_verify(data, signature, keyId);
		System.out.println("Signature correct: " + isCorrect);

	}
}