package com.bshara.cryptoserver.Utils.Entities;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/** IMPORTNAT, REPLACED LANG WITH LANG3 WITHOUT TESTING IT!!!!!!!!!!!!  */
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class EncryUtil {
	private static final Logger log = Logger.getLogger(EncryUtil.class);

	public static String handleRSA(TreeMap<String, Object> map, String privateKey) {
		StringBuffer sbuffer = new StringBuffer();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			sbuffer.append(entry.getValue());
		}
		String signTemp = sbuffer.toString();

		String sign = "";
		if (StringUtils.isNotEmpty(privateKey)) {
			sign = RSA.sign(signTemp, privateKey);
		}
		return sign;
	}

	public static boolean checkDecryptAndSign(String data, String encrypt_key, String clientPublicKey,
			String serverPrivateKey) throws Exception {

		String AESKey = "";
		try {
			AESKey = RSA.decrypt(encrypt_key, serverPrivateKey);
		} catch (Exception e) {
			e.printStackTrace();

			log.error(e.getMessage(), e);
			return false;
		}

		String realData = ConvertUtils.hexStringToString(AES.decryptFromBase64(data, AESKey));

		TreeMap<String, String> map = JSON.parseObject(realData, new TypeReference<TreeMap<String, String>>() {
		});

		String sign = StringUtils.trimToEmpty(map.get("sign"));

		StringBuffer signData = new StringBuffer();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();

			if (StringUtils.equals((String) entry.getKey(), "sign")) {
				continue;
			}
			signData.append(entry.getValue() == null ? "" : entry.getValue());
		}

		boolean result = RSA.checkSign(signData.toString(), sign, clientPublicKey);

		return result;
	}

	public static String handleHmac(TreeMap<String, String> map, String hmacKey) {
		StringBuffer sbuffer = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			sbuffer.append(entry.getValue());
		}
		String hmacTemp = sbuffer.toString();

		String hmac = "";
		if (StringUtils.isNotEmpty(hmacKey)) {
			hmac = Digest.hmacSHASign(hmacTemp, hmacKey, Digest.ENCODE);
		}
		return hmac;
	}
}