package com.bshara.cryptoserver.Utils.Entities;

import java.util.TreeMap;

public class MessageBuilder {

	MessageBuilder instance;
	TreeMap<String, Object> data;
	
	public static Message createMessage() {
		return new Message();
	}
	

	
}
