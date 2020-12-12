package com.bshara.cryptoserver.Utils.Entities;



import java.util.Base64;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.bshara.cryptoserver.Utils.MathUtil;

public class RSAAlg {

    private long n, d, e, eulerVal, p, q;

    private static final int RANGE = 100;

    private static final int SPLIT_POINT = 2 << 8;

    private static final int PRIMALITY_TESTING_ROUNDS = 4;

    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();

    public RSAAlg() {
        Random random = ThreadLocalRandom.current();

        long p = MathUtil.probablePrime(RANGE, PRIMALITY_TESTING_ROUNDS), q;
        do {
            q = MathUtil.probablePrime(RANGE, PRIMALITY_TESTING_ROUNDS);
        } while (p == q);

        long eulerVal = (p - 1) * (q - 1);


        do {
            e = Math.abs(random.nextLong()) % eulerVal + 1;
        }while (MathUtil.gcd(e, eulerVal) != 1);


        d = (MathUtil.linearCongruence(e, 1, eulerVal)) % eulerVal;

        this.p = p;
        this.q = q;
        this.n = p * q;
        this.eulerVal = eulerVal;
    }

   
    public String encrypt(String plaintext) {
        int[] plaintextBytes = changeToInts(plaintext.toCharArray());
        StringBuilder builder = new StringBuilder();
        for (int plaintextByte : plaintextBytes) {
            int modExpResult = (int) MathUtil.modExpNonRec(plaintextByte, e, n);
            builder.append((char) (modExpResult / SPLIT_POINT)).
                    append((char) (modExpResult % SPLIT_POINT));
        }
        return encoder.encodeToString(builder.toString().getBytes());
    }

  
    public String decrypt(String cipher) {

        char[] cipherChars = new String(decoder.decode(cipher)).toCharArray();


        int[] cipherInts = new int[cipherChars.length / 2];
        for(int i = 0; i < cipherInts.length; i++)
            cipherInts[i] = (int)cipherChars[i * 2] * SPLIT_POINT
                    + (int)cipherChars[i * 2 + 1];

        int[] plaintextInts = new int[cipherInts.length];
        for(int i = 0; i < cipherInts.length; i++)
            plaintextInts[i] = (int)MathUtil.modExpNonRec(cipherInts[i], d, n);

        StringBuilder plainText = new StringBuilder();
        for (int plaintextInt : plaintextInts)
            plainText.append((char) plaintextInt);
        return plainText.toString();
    }

 
    private int[] changeToInts(char[] chars) {
        if (chars != null && chars.length > 0) {
            int[] result = new int[chars.length];
            for (int i = 0; i < chars.length; i++) {
                result[i] = chars[i];
            }
            return result;
        }
        return null;
    }
  
}