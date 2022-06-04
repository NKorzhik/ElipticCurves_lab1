package org.kpi;

import java.math.BigInteger;
import java.util.Random;

public class AffinePoint {

    private BigInteger x;
    private BigInteger y;

    public AffinePoint() {
    }

    public AffinePoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return  " x = " + x +
                " y = " + y ;
    }
}
