package org.kpi;

import java.math.BigInteger;
import java.util.Objects;

public class ProjectivePoint {

    private final BigInteger x;
    private final BigInteger y;
    private final BigInteger z;

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


    public BigInteger getY() {
        return y;
    }

    public BigInteger getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectivePoint point = (ProjectivePoint) o;
        return x.equals(point.x) && y.equals(point.y) && z.equals(point.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "ProjectivePoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
