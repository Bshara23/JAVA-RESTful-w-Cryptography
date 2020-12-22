package com.bshara.cryptoserver.Utils;

// Modular Multiplicative Inverse calculator
// https://planetcalc.com/3311/

// LCM calculator
// https://www.mathsisfun.com/least-common-multiple-tool.html

// co prime calculator
// https://www.mathsisfun.com/numbers/coprime-calculator.html

public enum Keys {
	INSTANCE;

	public Key getKey(int i) {
		Key k = new Key();
		switch (i) {
		case 0:

			k.p = 61;
			k.q = 53;
			k.n = k.p * k.q; // 3233
			// theta = 60*52=3120
			k.e = 17;
			k.d = 413;

			break;

		case 1:

			k.p = 67;
			k.q = 53;
			k.n = k.p * k.q; // 3551
			// lcm(p-1,q-1) = 1716
			// theta = 66*52=3432
			k.e = 19; // co-prime to 1716
			k.d = 1987; // mod inverse to e mod theta = 19*x = 1 % 3432

			break;

		case 2:

			k.p = 71;
			k.q = 61;
			k.n = k.p * k.q; // 4331
			// lcm(p-1,q-1) = lcm(70, 60) = 420
			// theta = 70*60=4200
			k.e = 53; // co-prime to 420
			k.d = 317; // mod inverse to e mod theta = 53*x = 1 % 4200

			break;

		default:
			k.p = 67;
			k.q = 53;
			k.n = k.p * k.q; // 3551
			// lcm(p-1,q-1) = 1716
			// theta = 66*52=3432
			k.e = 19; // co-prime to 1716
			k.d = 1987; // mod inverse to e mod theta = 19*x = 1 % 3432
			break;
		}
		return k;
	}

	public class Key {

		public int n;
		public int p;
		public int q;
		public int phi;
		public int e;
		public int d;

	}
}
