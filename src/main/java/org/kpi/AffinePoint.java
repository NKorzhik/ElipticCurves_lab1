package org.kpi;

import java.math.BigInteger;

public record AffinePoint(BigInteger x, BigInteger y) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffinePoint point = (AffinePoint) o;
        return x.equals(point.x) && y.equals(point.y);
    }

    @Override
    public String toString() {
        return "AffinePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
