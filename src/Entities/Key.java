package Entities;

import java.io.Serializable;
import java.security.KeyPair;

public class Key implements Serializable {

	private static final long serialVersionUID = 1L;

	private KeyPair keyPair;
	private String keyId;

	public Key(String keyId, KeyPair keyPair) {
		this.keyPair = keyPair;
		this.keyId = keyId;
	}

	public Key() {
	}

	
	public KeyPair getKeyPair() {
		return keyPair;
	}

	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	

	@Override
	public String toString() {
		return "Key [keyPair=" + keyPair + ", keyId=" + keyId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyId == null) ? 0 : keyId.hashCode());
		result = prime * result + ((keyPair == null) ? 0 : keyPair.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		if (keyId == null) {
			if (other.keyId != null)
				return false;
		} else if (!keyId.equals(other.keyId))
			return false;
		if (keyPair == null) {
			if (other.keyPair != null)
				return false;
		} else if (!keyPair.equals(other.keyPair))
			return false;
		return true;
	}

	
	

}
