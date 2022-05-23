package org.example;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) throws Exception {

        // P - 192
        BigInteger a = BigInteger.valueOf(-3);
        BigInteger b = new BigInteger("2455155546008943817740293915197451784769108058161191238065");
        BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        BigInteger n = new BigInteger("6277101735386680763835789423176059013767194773182842284081");

        //224
//        BigInteger a = BigInteger.valueOf(-3);
//        BigInteger p = new BigInteger("26959946667150639794667015087019630673557916260026308143510066298881");
//        BigInteger b = new BigInteger("18958286285566608000408668544493926415504680968679321075787234672564");
//        BigInteger n = new BigInteger("26959946667150639794667015087019625940457807714424391721682722368061");

        String s = n.toString(2);


        if (BigInteger.valueOf(4)
                .multiply(b.pow(3))
                .add(BigInteger.valueOf(27)
                        .multiply(a.pow(2)))
                .mod(p)
                .equals(BigInteger.ZERO)) {
            throw new Exception("not smooth");
        }


        AffinePoint point1 = AffinePoint.createAffinePoint(a, b, p);
        AffinePoint point2 = AffinePoint.createAffinePoint(a, b, p);


        ProjectivePoint point3 = AffinePoint.affineToProjective(point1);
        ProjectivePoint point4 = AffinePoint.affineToProjective(point2);

        //System.out.println(ProjectivePoint.pointAdd(point4,point3,a,p));

        System.out.println(ProjectivePoint.scalarMultiplicationM(point3, n, a, p));


    }


}