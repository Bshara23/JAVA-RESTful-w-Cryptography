package com.bshara.cryptoserver.Utils.Entities;

import java.util.TreeMap;

public class Message {
	TreeMap<String, Object> data;

	public Message() {
		this.data = new TreeMap<String, Object>();
	}
	
	public TreeMap<String, Object> build(){
		return data;
	}
	
	public Message add(String key, Object value) {
		
		
		data.put(key, value);
		
		return this;
	}
}