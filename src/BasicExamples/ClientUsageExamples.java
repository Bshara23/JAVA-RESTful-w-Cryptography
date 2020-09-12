package BasicExamples;

import java.awt.List;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;

import org.json.JSONException;

import Entities.Key;
import Entities.Message;
import Utils.ClientUtil;
import Utils.CryptoUtil;
import Utils.JSONUtil;

// http://localhost:8080/JRA2/main?a=5
public class ClientUsageExamples {

	public static void main(String[] args) throws IOException, JSONException {

		generateKeys();
	}




	static void generateKeys() throws IOException {

		Message str;
		String url = "http://localhost:8080/JRA2/main/generate?size=2048";

		str = JSONUtil.postToUrl(url, Message.class);
		System.out.println(str);

		str = JSONUtil.postToUrl(url, Message.class);
		System.out.println(str);

		str = JSONUtil.postToUrl(url, Message.class);
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
		String keyId = ClientUtil.generateKey();
		String data = "this-is-an-example-message-for-tests";

		String encryptedData = ClientUtil.encrypt(keyId, data);
		System.out.println(encryptedData);
		// String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
	}

	static void encryptAndDecryptMessage() throws IOException {
		String keyId = ClientUtil.generateKey();
		String data = "this-is-an-example-message-for-tests";

		String encryptedData = ClientUtil.encrypt(keyId, data);
		System.out.println(encryptedData);

		String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
		System.out.println(decryptedData);

		// String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
	}

	static void signAndVerify() throws IOException {
		String keyId = ClientUtil.generateKey();
		String data = "this-is-an-example-message-for-tests";

		String signature = ClientUtil.sign(data, keyId);

		boolean isCorrect = ClientUtil.verify(data, signature, keyId);
		System.out.println("Signature correct: " + isCorrect);

	}
}