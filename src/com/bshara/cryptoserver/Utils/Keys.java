package com.bshara.cryptoserver.Utils;

public enum Keys {
	INSTANCE;
	
	public Key getKey(int i) {
		Key k = new Key();
		switch (i) {
		case 0:
			k.n = 812666190319L;
			k.p = 901471L;
			k.q = 901489L;
			k.phi = 812664387360L;
			k.e = 645476113909L;
			k.d = 740502560989L;
			break;

			
		case 1:
			k.n = 812702249999L;
			k.p = 901499L;
			k.q = 901501L;
			k.phi = 812700447000L;
			k.e = 129669903751L;
			k.d = 682093923751L;
			
			break;
			
		case 2:
			k.n = 812729295221L;
			k.p = 901513L;
			k.q = 901517L;
			k.phi = 812727492192L;
			k.e = 194647095743L;
			k.d = 671399877215L;
			break;
		default:
			break;
		}
		return k;
	}


	public class Key {
		
		public long n;
		public long p;
		public long q;
		public long phi;
		public long e;
		public long d;
		
	}
}
