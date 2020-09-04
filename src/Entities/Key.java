package Entities;

import java.io.Serializable;

public class Key implements Serializable{

	public String encryptedData;
	public String keyId;
	
	public Key(String encryptedData, String keyId) {
		this.encryptedData = encryptedData;
		this.keyId = keyId;
	}
	
	
	public Key() {}


	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}


	@Override
	public String toString() {
		return "Key [encryptedData=" + encryptedData + ", keyId=" + keyId + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((encryptedData == null) ? 0 : encryptedData.hashCode());
		result = prime * result + ((keyId == null) ? 0 : keyId.hashCode());
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
		if (encryptedData == null) {
			if (other.encryptedData != null)
				return false;
		} else if (!encryptedData.equals(other.encryptedData))
			return false;
		if (keyId == null) {
			if (other.keyId != null)
				return false;
		} else if (!keyId.equals(other.keyId))
			return false;
		return true;
	}
	
	
	
}
