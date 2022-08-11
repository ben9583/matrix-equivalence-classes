package impl.utils;

import java.math.BigInteger;

public class MathUtils {
    public static BigInteger euclid(BigInteger x, BigInteger y) {
        if (y.equals(BigInteger.ZERO))
            return x;

        return euclid(y, x.mod(y));
    }

    public static int euclid(int x, int y) {
        if (y == 0)
            return x;

        return euclid(y, x % y);
    }

    public static BigInteger lcm(BigInteger x, BigInteger y) {
        if (x.equals(BigInteger.ZERO) || y.equals(BigInteger.ZERO))
            return BigInteger.ZERO;

        return x.multiply(y).divide(euclid(x, y));
    }

    public static int lcm(int x, int y) {
        if (x == 0 || y == 0)
            return 0;

        return x * y / euclid(x, y);
    }
}
