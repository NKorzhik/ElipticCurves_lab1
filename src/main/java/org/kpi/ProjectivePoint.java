package org.kpi;


import java.math.BigInteger;


public class ProjectivePoint {

    private BigInteger x;
    private BigInteger y;
    private BigInteger z;
    final static ProjectivePoint POINT_AT_INFINITY = new ProjectivePoint(BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO);

    public ProjectivePoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
        this.z = BigInteger.ONE;
    }

    public ProjectivePoint(BigInteger x, BigInteger y, BigInteger z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }

    public BigInteger getZ() {
        return z;
    }

    public void setZ(BigInteger z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return  " x = " + x +
                " y = " + y +
                " z = " + z;
    }
}
