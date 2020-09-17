package com.bshara.cryptoserver.Entities;

public class SignedMessage {

	private String content;
	private boolean isVerified;
	public SignedMessage(String content, boolean isVerified) {
		super();
		this.content = content;
		this.isVerified = isVerified;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isVerified() {
		return isVerified;
	}
	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	@Override
	public String toString() {
		return "SignedMessage [content=" + content + ", isVerified=" + isVerified + "]";
	}
	
	
}
