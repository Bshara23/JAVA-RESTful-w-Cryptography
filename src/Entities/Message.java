package Entities;

import java.io.Serializable;

public class Message  implements Serializable{

	
	private String content;
	public Message() {
		this.content = "";
	}
	
	public Message(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Message [content=" + content + "]";
	}
	
	
	
	
}
