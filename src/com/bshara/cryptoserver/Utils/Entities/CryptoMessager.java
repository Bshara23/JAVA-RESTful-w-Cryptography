package com.bshara.cryptoserver.Utils.Entities;

import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CryptoMessager {
	
	
	@SuppressWarnings("unchecked")
	public static TreeMap<String, Object> Encrypt(TreeMap<String, Object> messageObject, String myPrivateKey,
			String recipientPublicKey) throws Exception {
		TreeMap<String, Object> signedEncryptedMessage = (TreeMap<String, Object>) messageObject.clone();

		// Create a RSA signature on the given message using own decryption key
		String sign = EncryUtil.handleRSA(signedEncryptedMessage, myPrivateKey);
		signedEncryptedMessage.put("sign", sign); // add the signature to the message

		String JSONMessage = JSON.toJSONString(signedEncryptedMessage);

		// Randomly generate AES key
		String aesKey = SecureRandomUtil.getRandom(16);

		// encrypt the message along with the signature using the AES key
		String encryptedMessage = AES.encryptToBase64(ConvertUtils.stringToHexString(JSONMessage), aesKey);

		// Use the RSA algorithm to encrypt the AES key that has just been generated
		String encryptedAESkey = RSA.encrypt(aesKey, recipientPublicKey);
		//String encryptedAESkey = RSAAlg.encrypt(aesKey, recipientPublicKey);
		
		
		
		signedEncryptedMessage = new TreeMap<String, Object>();
		signedEncryptedMessage.put("encryptedMessage", encryptedMessage);
		signedEncryptedMessage.put("encryptedAESkey", encryptedAESkey);

		return signedEncryptedMessage;
	}
	
	@SuppressWarnings("unchecked")
	public static TreeMap<String, Object> Decrypt(TreeMap<String, Object> signedEncryptedMessage,
			String myPrivateKey, String recipientPublicKey) throws Exception {

		TreeMap<String, Object> decryptedMessage = new TreeMap<String, Object>();

		String encryptedMessage = (String) signedEncryptedMessage.get("encryptedMessage");
		String encryptedAESkey = (String) signedEncryptedMessage.get("encryptedAESkey");

		// Verify
		boolean passSign = EncryUtil.checkDecryptAndSign(encryptedMessage, encryptedAESkey, recipientPublicKey,
				myPrivateKey);

		// Pass the verification
		if (passSign) {

			String aeskey = RSA.decrypt(encryptedAESkey, myPrivateKey);
			String data = ConvertUtils.hexStringToString(AES.decryptFromBase64(encryptedMessage, aeskey));


			decryptedMessage = JSONObject.parseObject(data, TreeMap.class);
			decryptedMessage.remove("sign");

		}
		return decryptedMessage;
	}
}
