package BasicExamples;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import Entities.Key;
import Entities.Message;
import Utils.ClientUtil;
import Utils.JSONUtil;

// http://localhost:8080/JRA2/main?a=5
public class ClientUsageExamples {

	public static void main(String[] args) throws IOException, JSONException {

		f6();
	}

	static void f2() throws IOException {
		String url0 = "http://localhost:8080/JRA2/main?a=5";
		String url1 = "http://localhost:8080/JRA2/main";

		Key myKey = JSONUtil.getObjFromUrl(url1, Key.class);
		System.out.println(myKey);

	}

	static void f1() throws IOException {
		// MyUtil.postToUrl("http://localhost:8080/JRA2/main/post/wewewewqe?data=adwdwad");
	}

	static void verify() throws IOException {
		// verify/awd?data=awd&signature=awd
		Message msg = JSONUtil.postToUrl("http://localhost:8080/JRA2/main/verify/aaa?data=bbb&signature=ccc",
				Message.class);
		System.out.println(msg.getContent());
	}

	static void f3() throws IOException {

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

	static void f4() throws IOException {
		ArrayList<String> keys = JSONUtil.getArrayList("http://localhost:8080/JRA2/main/", String.class);
		System.out.println("number of keys=" + keys.size());


		for (int i = 0; i < keys.size(); i++) {
			System.out.println(keys.get(i));
		}

	}
	
	static void f5() throws IOException {
		String keyId = ClientUtil.generateKey();
		String data = "this-is-an-example-message-for-tests";
		
		String encryptedData = ClientUtil.encrypt(keyId, data);
		System.out.println(encryptedData);
		//String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
	}
	
	static void f6() throws IOException {
		String keyId = ClientUtil.generateKey();
		String data = "this-is-an-example-message-for-tests";
		
		String encryptedData = ClientUtil.encrypt(keyId, data);
		System.out.println(encryptedData);
		
		String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
		System.out.println(decryptedData);

		//String decryptedData = ClientUtil.decrypt(keyId, encryptedData);
	}

}