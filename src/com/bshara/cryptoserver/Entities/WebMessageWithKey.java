package com.bshara.cryptoserver.Entities;

public class WebMessageWithKey extends WebMessage {
	
    private String publicKey;

	public WebMessageWithKey(String content, String publicKey) {
		super(content);
		this.publicKey = publicKey;
	}

	public WebMessageWithKey() {
		super("");
		this.publicKey = "";
	}
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		return "WebMessageWithKey [publicKey=" + publicKey + "]";
	}

}
