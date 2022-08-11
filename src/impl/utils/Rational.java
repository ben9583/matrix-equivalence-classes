package impl.utils;

import java.math.BigInteger;

public class Rational {
    public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);
    public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;
    public Rational(BigInteger numerator, BigInteger denominator) {
        if(denominator.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }

        // Store the number in reduced form
        BigInteger gcd = MathUtils.euclid(numerator, denominator);
        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
        if(denominator.compareTo(BigInteger.ZERO) < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Rational(Rational r) {
        this(r.numerator, r.denominator);
    }

    public Rational add(Rational other) {
        BigInteger numerator = this.numerator.multiply(other.denominator).add(other.numerator.multiply(this.denominator));
        BigInteger denominator = this.denominator.multiply(other.denominator);
        return new Rational(numerator, denominator);
    }

    public Rational multiply(Rational other) {
        BigInteger numerator = this.numerator.multiply(other.numerator);
        BigInteger denominator = this.denominator.multiply(other.denominator);
        return new Rational(numerator, denominator);
    }

    public Rational subtract(Rational other) {
        BigInteger numerator = this.numerator.multiply(other.denominator).subtract(other.numerator.multiply(this.denominator));
        BigInteger denominator = this.denominator.multiply(other.denominator);
        return new Rational(numerator, denominator);
    }

    public Rational divide(Rational other) {
        BigInteger numerator = this.numerator.multiply(other.denominator);
        BigInteger denominator = this.denominator.multiply(other.numerator);
        return new Rational(numerator, denominator);
    }

    public String toString() {
        return numerator + "/" + denominator;
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rational rational = (Rational) o;
        return numerator.equals(rational.numerator) &&
                denominator.equals(rational.denominator);
    }
}
