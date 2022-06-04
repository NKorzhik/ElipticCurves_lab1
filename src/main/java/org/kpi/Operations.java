package org.kpi;

import java.math.BigInteger;
import java.util.Random;

public class Operations {

    public Boolean isSmooth(BigInteger a, BigInteger b, BigInteger p) {

        return !BigInteger.valueOf(4)
                .multiply(b.pow(3))
                .add(BigInteger.valueOf(27)
                        .multiply(a.pow(2)))
                .mod(p)
                .equals(BigInteger.ZERO);

    }

    public AffinePoint createAffinePoint(BigInteger a, BigInteger b, BigInteger p) {

        AffinePoint result = new AffinePoint(BigInteger.valueOf(-1), BigInteger.valueOf(-1));

        while (!existPoint(result, a, b, p)) {
            BigInteger xPoint = getRandomBigInteger();
            BigInteger yPoint = sqrtMod((((xPoint.pow(3)).add(a.multiply(xPoint))).add(b)).mod(p), p);
            result = new AffinePoint(xPoint, yPoint);
        }

        return result;
    }

    public Boolean existPoint(AffinePoint point, BigInteger a, BigInteger b, BigInteger p) {
        BigInteger left = point.getY().modPow(BigInteger.valueOf(2), p);
        BigInteger right = point.getX().pow(3).add(a.multiply(point.getX())).add(b).mod(p);

        return left.equals(right);
    }


    public BigInteger getRandomBigInteger() {
        Random rand = new Random();
        return new BigInteger(192, rand);
    }

    public BigInteger sqrtMod(BigInteger a, BigInteger p) {
        BigInteger k = (p.subtract(BigInteger.valueOf(3))).shiftRight(2);
        return a.modPow(k.add(BigInteger.valueOf(1)), p);
    }


    public ProjectivePoint affineToProjective(AffinePoint point) {
        return new ProjectivePoint(point.getX(), point.getY());
    }

    public AffinePoint projectToAffine(ProjectivePoint point, BigInteger p) {
        if (point.equals(ProjectivePoint.POINT_AT_INFINITY)) {
            return new AffinePoint(null, null);
        }
        return new AffinePoint(point.getX().multiply(point.getZ().modInverse(p)).mod(p), point.getY().multiply(point.getZ().modInverse(p)).mod(p));
    }


    public ProjectivePoint pointDouble(ProjectivePoint point, BigInteger a, BigInteger p) {

        if (point.equals(ProjectivePoint.POINT_AT_INFINITY)) {
            return ProjectivePoint.POINT_AT_INFINITY;
        }

        if (point.getY().equals(BigInteger.ZERO)) {
            return ProjectivePoint.POINT_AT_INFINITY;
        }

        BigInteger W = a.multiply(point.getZ().pow(2)).add(BigInteger.valueOf(3).multiply(point.getX().pow(2))).mod(p);
        BigInteger S = point.getY().multiply(point.getZ()).mod(p);
        BigInteger B = point.getX().multiply(point.getY()).multiply(S).mod(p);
        BigInteger H = W.pow(2).subtract(BigInteger.valueOf(8).multiply(B)).mod(p);

        return new ProjectivePoint(
               H.multiply(S).shiftLeft(1).mod(p),
                //BigInteger.valueOf(2).multiply(H).multiply(S).mod(p),
                W.multiply(BigInteger.valueOf(4).multiply(B).subtract(H)).subtract(BigInteger.valueOf(8).multiply(point.getY().pow(2)).multiply(S.pow(2))).mod(p),
                BigInteger.valueOf(8).multiply(S.pow(3)).mod(p));


    }

    public ProjectivePoint pointAdd(ProjectivePoint pointA, ProjectivePoint pointB, BigInteger a, BigInteger p) {

        if (pointA.equals(ProjectivePoint.POINT_AT_INFINITY)) {
            return pointB;
        }

        if (pointB.equals(ProjectivePoint.POINT_AT_INFINITY)) {
            return pointA;
        }

        BigInteger U1 = pointB.getY().multiply(pointA.getZ()).mod(p);
        BigInteger U2 = pointA.getY().multiply(pointB.getZ()).mod(p);

        BigInteger V1 = pointB.getX().multiply(pointA.getZ()).mod(p);
        BigInteger V2 = pointA.getX().multiply(pointB.getZ()).mod(p);

        if (V1.equals(V2)) {
            if (!U1.equals(U2)) {
                return ProjectivePoint.POINT_AT_INFINITY;
            } else return pointDouble(pointA, a, p);
        }

        BigInteger U = (U1.subtract(U2)).mod(p);
        BigInteger V = (V1.subtract(V2)).mod(p);

        BigInteger W = pointA.getZ().multiply(pointB.getZ()).mod(p);
        BigInteger A = U.pow(2).multiply(W).subtract(V.pow(3)).subtract(BigInteger.valueOf(2).multiply(V.pow(2)).multiply(V2)).mod(p);


        return new ProjectivePoint(V.multiply(A).mod(p),
                U.multiply(V.pow(2).multiply(V2).subtract(A)).subtract(V.pow(3).multiply(U2)).mod(p),
                V.pow(3).multiply(W).mod(p));
    }

    public ProjectivePoint scalarMultiplicationM(ProjectivePoint point, BigInteger n, BigInteger a, BigInteger p) {

        ProjectivePoint R0 = ProjectivePoint.POINT_AT_INFINITY;
        ProjectivePoint R1 = point;

        String s = n.toString(2);

        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == 0) {
                R1 = pointAdd(R0, R1, a, p);
                R0 = pointDouble(R0, a, p);
            } else {
                R0 = pointAdd(R0, R1, a, p);
                R1 = pointDouble(R1, a, p);
            }

        }
        return new ProjectivePoint(R0.getX().mod(p), R0.getY().mod(p), R0.getZ().mod(p));
    }


}
