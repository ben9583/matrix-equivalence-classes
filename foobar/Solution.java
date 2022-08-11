package disorderly_escape;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Solution {
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

    public static class Rational {
        public static final Rational ONE = new Rational(BigInteger.ONE, BigInteger.ONE);
        public static final Rational ZERO = new Rational(BigInteger.ZERO, BigInteger.ONE);

        private final BigInteger numerator;
        private final BigInteger denominator;
        public Rational(BigInteger numerator, BigInteger denominator) {
            if(denominator.equals(BigInteger.ZERO)) {
                throw new IllegalArgumentException("Denominator cannot be zero");
            }

            // Store the number in reduced form
            BigInteger gcd = euclid(numerator, denominator);
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
    public static class SymbolPair {
        public int subscript;
        public int exponent;
        public SymbolPair(int subscript, int exponent) {
            this.subscript = subscript;
            this.exponent = exponent;
        }

        public SymbolPair(SymbolPair sp) {
            this.subscript = sp.subscript;
            this.exponent = sp.exponent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SymbolPair that = (SymbolPair) o;
            return subscript == that.subscript &&
                    exponent == that.exponent;
        }

        @Override
        public String toString(){
            return "(sub: " + subscript + ", exp: " + exponent + ")";
        }

        @Override
        public int hashCode() {
            return Objects.hash(subscript, exponent);
        }
    }
    public static class Symbol {
        private Rational rational;
        private final Set<SymbolPair> symbolPairs;

        public Symbol(Rational rational, Set<SymbolPair> symbolPairs) {
            this.rational = rational;
            this.symbolPairs = symbolPairs;
        }

        public Symbol(Symbol s) {
            this.rational = new Rational(s.rational);
            this.symbolPairs = new HashSet<>(s.symbolPairs.size());
            for(SymbolPair sp : s.symbolPairs) {
                this.symbolPairs.add(new SymbolPair(sp));
            }
        }

        public Symbol distribute(Symbol other) {
            SymbolPair[] newSymbolPairs = new SymbolPair[this.symbolPairs.size() * other.symbolPairs.size()];

            int count = 0;
            for (SymbolPair tsp : this.symbolPairs) {
                for (SymbolPair osp : other.symbolPairs) {
                    int thisOtherLcm = lcm(tsp.subscript, osp.subscript);
                    newSymbolPairs[count] = new SymbolPair(thisOtherLcm, tsp.exponent * osp.exponent * tsp.subscript * osp.subscript / thisOtherLcm);
                    count++;
                }
            }

            if(newSymbolPairs.length <= 1) {
                Set<SymbolPair> out = new HashSet<>(newSymbolPairs.length);
                Collections.addAll(out, newSymbolPairs);
                return new Symbol(this.rational.multiply(other.rational), out);
            }

            for(int i = 0; i < newSymbolPairs.length - 1; i++) {
                for(int j = i + 1; j < newSymbolPairs.length; j++) {
                    if(newSymbolPairs[i].subscript == newSymbolPairs[j].subscript && newSymbolPairs[i].exponent != -1) {
                        newSymbolPairs[i].exponent += newSymbolPairs[j].exponent;
                        newSymbolPairs[j].exponent = -1;
                    }
                }
            }

            Set<SymbolPair> out = new HashSet<>(newSymbolPairs.length);
            for(SymbolPair sp : newSymbolPairs) {
                if(sp.exponent != -1) {
                    out.add(sp);
                }
            }

            return new Symbol(this.rational.multiply(other.rational), out);
        }

        public Rational eval(int n) {
            BigInteger bigN = BigInteger.valueOf(n);

            int totalExponent = 0;
            for (SymbolPair sp : this.symbolPairs) {
                totalExponent += sp.exponent;
            }

            return this.rational.multiply(new Rational(bigN.pow(totalExponent), BigInteger.ONE));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Symbol symbol = (Symbol) o;
            return this.rational.equals(symbol.rational) &&
                    this.symbolPairs.equals(symbol.symbolPairs);
        }

        public static Symbol[] add(Symbol[] a, Symbol[] b) {
            Symbol[] result = new Symbol[a.length + b.length];
            for(int i = 0; i < a.length; i++) {
                result[i] = new Symbol(a[i]);
            }
            for(int i = 0; i < b.length; i++) {
                result[i + a.length] = new Symbol(b[i]);
            }

            for (int i = 0; i < result.length - 1; i++) {
                for (int j = i + 1; j < result.length; j++) {
                    if (result[i].symbolPairs.equals(result[j].symbolPairs)) {
                        result[i].rational = result[i].rational.add(result[j].rational);
                        result[j].rational = Rational.ZERO;
                    }
                }
            }

            return Arrays.stream(result).filter(s -> !s.rational.getNumerator().equals(BigInteger.ZERO)).toArray(Symbol[]::new);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append(rational.toString());
            sb.append(", [");
            for(SymbolPair sp : symbolPairs) {
                sb.append(sp.toString());
                sb.append(", ");
            }
            sb.append("])");
            return sb.toString();
        }

        public static Symbol[] multiply(int subscript, Symbol[] symbols) {
            Symbol[] result = new Symbol[symbols.length];
            for(int i = 0; i < result.length; i++) {
                result[i] = new Symbol(symbols[i]);
            }

            for(Symbol s : result) {
                boolean added = false;
                for(SymbolPair sp : s.symbolPairs) {
                    if(sp.subscript == subscript) {
                        added = true;
                        sp.exponent++;
                        break;
                    }
                }

                if(!added) {
                    s.symbolPairs.add(new SymbolPair(subscript, 1));
                }
            }

            return result;
        }
        
        public static Symbol[] distributeRational(Rational r, Symbol[] symbols) {
            Symbol[] result = new Symbol[symbols.length];
            for(int i = 0; i < result.length; i++) {
                result[i] = new Symbol(symbols[i]);
            }

            for (Symbol symbol : result) {
                symbol.rational = symbol.rational.multiply(r);
            }
            return result;
        }
    }

    public static Symbol[][] symbolCache = new Symbol[13][];

    public static Symbol[] generateTerms(int x) {
        if(x == 0) return new Symbol[]{new Symbol(Rational.ONE, new HashSet<>())};

        if(symbolCache[x] != null) return symbolCache[x];

        Symbol[] symbols = new Symbol[0];
        for(int i = 1; i <= x; i++) {
            symbols = Symbol.add(symbols, Symbol.multiply(i, generateTerms(x - i)));
        }
        
        symbolCache[x] = Symbol.distributeRational(new Rational(BigInteger.ONE, BigInteger.valueOf(x)), symbols);
        return symbolCache[x];
    }

    public static Symbol[] generateTerms(int w, int h) {
        Symbol[] symbolsB = generateTerms(h);
        Symbol[] symbolsA = generateTerms(w);

        Symbol[] symbols = new Symbol[symbolsA.length * symbolsB.length];
        int count = 0;
        for(Symbol symA : symbolsA) {
            for(Symbol symB : symbolsB) {
                symbols[count] = symA.distribute(symB);
                count++;
            }
        }

        return symbols;
    }

    public static String solution(int w, int h, int s) {
        // Your code here
        Symbol[] symbols = generateTerms(w, h);

        Rational total = Rational.ZERO;
        for(Symbol sym : symbols) {
            total = total.add(sym.eval(s));
        }

        return total.getNumerator().divide(total.getDenominator()).toString();
    }

    public static Map<Integer[], String> tests = new HashMap<>();

    public static void main(String[] args) {
        /*
        tests.put(new Integer[] {1, 1, 1}, "1");
        tests.put(new Integer[] {1, 1, 2}, "2");
        tests.put(new Integer[] {1, 1, 3}, "3");
        tests.put(new Integer[] {1, 1, 4}, "4");
        tests.put(new Integer[] {2, 2, 1}, "1");
        tests.put(new Integer[] {2, 2, 2}, "7");
        tests.put(new Integer[] {3, 3, 2}, "36");
        */
        tests.put(new Integer[] {3, 2, 2}, "13");
        tests.put(new Integer[] {2, 3, 2}, "13");
        tests.put(new Integer[] {3, 3, 2}, "36");
        tests.put(new Integer[] {3, 2, 3}, "92");
        tests.put(new Integer[] {12, 12, 20}, "97195340925396730736950973830781340249131679073592360856141700148734207997877978005419735822878768821088343977969209139721682171487959967012286474628978470487193051591840");

        for(Map.Entry<Integer[], String> kv : tests.entrySet()) {
            Integer[] input = kv.getKey();
            String resp = solution(input[0], input[1], input[2]);
            if(resp.equals(kv.getValue())) {
                System.out.println("Passed test: " + Arrays.toString(input));
            } else {
                System.out.println("Failed test: " + Arrays.toString(input) + ". Expected \"" + kv.getValue() + "\" but got \"" + resp + "\"");
            }
        }
    }
}
