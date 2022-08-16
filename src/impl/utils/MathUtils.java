package impl.utils;

public class MathUtils {
    public static int euclid(int x, int y) {
        if (y == 0)
            return x;

        return euclid(y, x % y);
    }

    public static int lcm(int x, int y) {
        if (x == 0 || y == 0)
            return 0;

        return x * y / euclid(x, y);
    }
}
