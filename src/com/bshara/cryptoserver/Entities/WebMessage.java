package com.bshara.cryptoserver.Entities;

public class WebMessage {

	private String content;

	public WebMessage(String content) {
		super();
		this.content = content;
	}
	public WebMessage() {
		super();
		this.content = "";
	}
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "WebMessage [content=" + content + "]";
	}
	
	
}
