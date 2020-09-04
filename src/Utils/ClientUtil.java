package Utils;

import java.io.IOException;

import Entities.Message;

public class ClientUtil {
	
	public final static String root = "http://localhost:8080/JRA2/main/";

	
	public static String generateKey() throws IOException {
		String generateUrl = root + "generate?size=2048";
		String keyId = ((Message) JSONUtil.postToUrl(generateUrl, Message.class)).getContent();
		return keyId;
	}

	
	public static String encrypt(String keyId, String data) throws IOException {
		String encryptUrl = root + "encrypt/" + keyId + "?data=" + data;
		String encryptedData = ((Message) JSONUtil.postToUrl(encryptUrl, Message.class)).getContent();
		return encryptedData;
	}
	
	public static String decrypt(String keyId, String encryptedData) throws IOException {
		String decryptUrl = root + "decrypt/" + keyId + "?encryptedData=" + encryptedData;
		String decryptedData = ((Message) JSONUtil.postToUrl(decryptUrl, Message.class)).getContent();
		return decryptedData;
	}
}
