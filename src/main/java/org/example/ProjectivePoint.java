package org.example;


import java.math.BigInteger;


public class ProjectivePoint {
    BigInteger x;
    BigInteger y;
    BigInteger z;


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

    final static ProjectivePoint POINT_AT_INFINITY = new ProjectivePoint(BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO);


    public static AffinePoint projectToAffine(ProjectivePoint point, BigInteger p) {
        if (point.equals(POINT_AT_INFINITY)) {
            return new AffinePoint(null, null);
        }
        return new AffinePoint(point.x.multiply(point.z.modInverse(p)).mod(p), point.y.multiply(point.z.modInverse(p)).mod(p));
    }


    public static ProjectivePoint pointDouble(ProjectivePoint point, BigInteger a, BigInteger p) {

        if (point.equals(POINT_AT_INFINITY)) {
            return POINT_AT_INFINITY;
        }

        if (point.y.equals(BigInteger.ZERO)) {
            return POINT_AT_INFINITY;
        }

        BigInteger W = a.multiply(point.z.pow(2)).add(BigInteger.valueOf(3).multiply(point.x.pow(2))).mod(p);
        BigInteger S = point.y.multiply(point.z).mod(p);
        BigInteger B = point.x.multiply(point.y).multiply(S).mod(p);
        BigInteger H = W.pow(2).subtract(BigInteger.valueOf(8).multiply(B)).mod(p);

        return new ProjectivePoint(
                H.multiply(S).shiftLeft(1).mod(p),
                W.multiply(BigInteger.valueOf(4).multiply(B).subtract(BigInteger.valueOf(8))).multiply(point.y.pow(2).multiply(S.pow(2))).mod(p),
                BigInteger.valueOf(8).multiply(S.pow(3)).mod(p));


    }

    public static ProjectivePoint pointAdd(ProjectivePoint pointA, ProjectivePoint pointB, BigInteger a, BigInteger p) {

        if (pointA.equals(POINT_AT_INFINITY)) {
            return pointB;
        }

        if (pointB.equals(POINT_AT_INFINITY)) {
            return pointA;
        }

        BigInteger U1 = pointB.y.multiply(pointA.z).mod(p);
        BigInteger U2 = pointA.y.multiply(pointB.z).mod(p);

        BigInteger V1 = pointB.x.multiply(pointA.z).mod(p);
        BigInteger V2 = pointA.x.multiply(pointB.z).mod(p);

        if (V1.equals(V2)) {
            if (!U1.equals(U2)) {
                return POINT_AT_INFINITY;
            } else return pointDouble(pointA, a, p);
        }

        BigInteger U = (U1.subtract(U2)).mod(p);
        BigInteger V = (V1.subtract(V2)).mod(p);

        BigInteger W = pointA.z.multiply(pointB.z).mod(p);
        BigInteger A = U.pow(2).multiply(W).subtract(V.pow(3)).subtract(BigInteger.valueOf(2).multiply(V.pow(2)).multiply(V2)).mod(p);


        return new ProjectivePoint(V.multiply(A).mod(p),
                U.multiply(V.pow(2).multiply(V2).subtract(A)).subtract(V.pow(3).multiply(U2)).mod(p),
                V.pow(3).multiply(W).mod(p));
    }


    public static ProjectivePoint scalarMultiplicationM(ProjectivePoint point, BigInteger n, BigInteger a, BigInteger p) {

        ProjectivePoint R0 = POINT_AT_INFINITY;
        ProjectivePoint R1 = point;

        String s = n.toString(2);

        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == 0) {
                R1 = pointAdd(R1, R0, a, p);
                R0 = pointDouble(R0, a, p);
            } else {
                R0 = pointAdd(R0, R1, a, p);
                R1 = pointDouble(R1, a, p);
            }

        }
        return new ProjectivePoint(R0.x.mod(p), R0.y.mod(p), R0.z.mod(p));
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
