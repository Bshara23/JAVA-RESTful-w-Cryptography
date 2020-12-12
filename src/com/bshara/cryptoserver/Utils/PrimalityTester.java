package com.bshara.cryptoserver.Utils;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PrimalityTester {

    public static boolean isProbablePrime(long n, int rounds) {

        if (n == 2)
            return true;

        if ((n & 1) == 0)
            return false;

        long s = 0, d = n - 1, quotient, remainder;
        for (;;) {
            quotient = d / 2;
            remainder = d % 2;
            if (remainder == 1)
                break;
            s++;
            d = quotient;
        }

        Random rnd = ThreadLocalRandom.current();
        for (int i = 0; i < rounds; i++) {
            long a = Math.abs(rnd.nextLong()) % (n - 1) + 1;
            long b = MathUtil.modExpNonRec(a, d , n);

            if (b == 1 || b == n - 1) {
                continue;
            }
            int j = 0;
            for (; j < s; j++) {
                b = MathUtil.modExpNonRec(b, 2, n);
                if (b == 1) {
                    return false;
                }
                if (b == n - 1)
                    break;
            }
            if (j == s)
                return false;
        }
        return true;
    }
}