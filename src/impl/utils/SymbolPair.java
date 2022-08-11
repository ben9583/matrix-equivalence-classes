package impl.utils;

import java.util.Objects;

public class SymbolPair {
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
