package impl.utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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

        int count = 0;
        for (SymbolPair tsp : this.symbolPairs) {
            for (SymbolPair osp : other.symbolPairs) {
                int thisOtherLcm = MathUtils.lcm(tsp.subscript, osp.subscript);
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
