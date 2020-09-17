package com.bshara.cryptoserver.Entities;

import java.security.KeyPair;
import java.security.PublicKey;

public class TwoWayKeys {

	private KeyPair myKeys;
	private PublicKey othersPublicKey;
	public TwoWayKeys(KeyPair myKeys, PublicKey othersPublicKey) {
		super();
		this.myKeys = myKeys;
		this.othersPublicKey = othersPublicKey;
	}
	public KeyPair getMyKeys() {
		return myKeys;
	}
	public void setMyKeys(KeyPair myKeys) {
		this.myKeys = myKeys;
	}
	public PublicKey getOthersPublicKey() {
		return othersPublicKey;
	}
	public void setOthersPublicKey(PublicKey othersPublicKey) {
		this.othersPublicKey = othersPublicKey;
	}
	@Override
	public String toString() {
		return "TwoWayKeys [myKeys=" + myKeys + ", othersPublicKey=" + othersPublicKey + "]";
	}
	
	
	
	
}
