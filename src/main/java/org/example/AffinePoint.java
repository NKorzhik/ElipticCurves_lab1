package org.example;

import java.math.BigInteger;
import java.util.Random;

public class AffinePoint {

    BigInteger x;
    BigInteger y;

    public AffinePoint() {
    }

    public AffinePoint(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public static ProjectivePoint affineToProjective (AffinePoint point){
        return new ProjectivePoint(point.x,point.y);
    }


    public static Boolean existPoint(AffinePoint point, BigInteger a, BigInteger b, BigInteger p) {
        BigInteger left = point.y.modPow(BigInteger.valueOf(2), p);
        BigInteger right = (point.x.pow(3).add(a.multiply(point.x)).add(b)).mod(p);

        return left.equals(right);
    }

    public static BigInteger getRandomBigInteger() {
        Random rand = new Random();
        return new BigInteger(224, rand);
    }

    public static BigInteger sqrtMod(BigInteger a, BigInteger p) {
        BigInteger k = (p.subtract(BigInteger.valueOf(3))).shiftRight(2).add(BigInteger.valueOf(1));
        return a.modPow(k, p);
    }

    public static AffinePoint createAffinePoint(BigInteger a, BigInteger b, BigInteger p) {

        AffinePoint result = new AffinePoint(BigInteger.valueOf(-1), BigInteger.valueOf(-1));

        while (!existPoint(result, a, b, p)) {
            BigInteger xPoint = getRandomBigInteger();
            BigInteger yPoint = sqrtMod((xPoint.pow(3).add(a.multiply(xPoint)).add(b)).mod(p), p);
            result = new AffinePoint(xPoint, yPoint);
        }

        return result;
    }


    @Override
    public String toString() {
        return  " x = " + x +
                " y = " + y ;
    }
}
