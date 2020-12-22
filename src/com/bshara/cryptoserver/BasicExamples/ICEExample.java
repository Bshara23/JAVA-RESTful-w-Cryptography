package com.bshara.cryptoserver.BasicExamples;

import java.io.UnsupportedEncodingException;

import com.bshara.cryptoserver.Utils.Entities.Base64;
import com.bshara.cryptoserver.Utils.Entities.ICE;
import com.bshara.cryptoserver.Utils.Entities.SecureRandomUtil;

public class ICEExample {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String k = SecureRandomUtil.getRandom(8);
		k = "12345678";
		String m = "AAAEhAAACCcAAAgwAAAJZQAAB/4AAAY2AAAKbwAACm8AAAcTAAAIhQAABIcAAAcTAAAIjwAADFMAAARzAAAMOAAACKcAAAcTAAAL6QAAAA0AAAOWAAAE9gAAB3kAAAgnAAAEGAAABIQAAAmQAAAFvwAACDoAAAxUAAAAWQAACCcAAArTAAAAXwAABXAAAAFUAAAFiQAABSYAAAg6AAAGNQAABIQAAARzAAACTAAACvY=";

        m = new String(Base64.encode(m.getBytes()));
		System.out.println(m);

		
		
		String c = ICE.Encrypt(m, k);
	

		String m2 = ICE.Decrypt(c, k);
		System.out.println(m2);

		m2 = new String(Base64.decode(m2));

		
		
		System.out.println(m2);
		
	}
	

}
