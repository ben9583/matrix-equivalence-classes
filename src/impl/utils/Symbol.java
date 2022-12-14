package impl.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Symbol {
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
        if(newSymbolPairs.length == 0) return new Symbol(this.rational.multiply(other.rational), new HashSet<>(0));

        int count = 0;

        for (SymbolPair tsp : this.symbolPairs) {
            for (SymbolPair osp : other.symbolPairs) {
                int thisOtherLcm = MathUtils.lcm(tsp.subscript, osp.subscript);
                newSymbolPairs[count++] = new SymbolPair(thisOtherLcm, tsp.exponent * osp.exponent * tsp.subscript * osp.subscript / thisOtherLcm);
            }
        }

        if(newSymbolPairs.length == 1) {
            Set<SymbolPair> out = new HashSet<>(1);
            out.add(newSymbolPairs[0]);
            return new Symbol(this.rational.multiply(other.rational), out);
        }

        for(int i = 0; i < newSymbolPairs.length - 1; i++) {
            for(int j = i + 1; j < newSymbolPairs.length; j++) {
                if(newSymbolPairs[i].subscript == newSymbolPairs[j].subscript && newSymbolPairs[i].exponent != -1) {
                    newSymbolPairs[j].exponent += newSymbolPairs[i].exponent;
                    newSymbolPairs[i] = null;
                    break;
                }
            }
        }

        Set<SymbolPair> out = new HashSet<>(newSymbolPairs.length);
        Arrays.stream(newSymbolPairs)
                .filter(Objects::nonNull)
                .forEach(out::add);

        return new Symbol(this.rational.multiply(other.rational), out);
    }

    public Rational eval(int n) {
        BigInteger bigN = BigInteger.valueOf(n);

        int totalExponent = 0;
        for (SymbolPair sp : this.symbolPairs) {
            totalExponent += sp.exponent;
        }

        return this.rational.multiply(new Rational(bigN.pow(totalExponent)));
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
                    result[j].rational = result[j].rational.add(result[i].rational);
                    result[i] = null;
                    break;
                }
            }
        }

        return Arrays.stream(result)
                .filter(Objects::nonNull)
                .toArray(Symbol[]::new);
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
        return Arrays.stream(symbols)
                .map(Symbol::new)
                .peek(s -> s.symbolPairs
                        .stream()
                        .filter(sp -> sp.subscript == subscript)
                        .findAny()
                        .ifPresentOrElse(
                                sp -> sp.exponent++,
                                () -> s.symbolPairs.add(new SymbolPair(subscript, 1))
                        )
                ).toArray(Symbol[]::new);
    }

    public static Symbol[] distributeRational(Rational r, Symbol[] symbols) {
        return Arrays.stream(symbols)
                .map(s -> new Symbol(s.rational.multiply(r), s.symbolPairs))
                .toArray(Symbol[]::new);
    }
}
