package impl;

import impl.utils.DisorderlyEscapeSolutionInput;
import impl.utils.Rational;
import impl.utils.Symbol;

import java.math.BigInteger;
import java.util.HashSet;

public class Solution implements ISolution<DisorderlyEscapeSolutionInput, String> {
    public static Symbol[][] symbolCache = new Symbol[128][];

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

    public String solution(DisorderlyEscapeSolutionInput input) {
        int w = input.width();
        int h = input.height();
        int s = input.states();

        Symbol[] symbols = generateTerms(w, h);

        Rational total = Rational.ZERO;
        for(Symbol sym : symbols) {
            total = total.add(sym.eval(s));
        }

        return total.getNumerator().divide(total.getDenominator()).toString();
    }
}
