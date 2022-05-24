package org.example;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) throws Exception {

        // P - 192
        BigInteger a = BigInteger.valueOf(-3);
        BigInteger b = new BigInteger("2455155546008943817740293915197451784769108058161191238065");
        BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        BigInteger n = new BigInteger("6277101735386680763835789423176059013767194773182842284081");


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

        System.out.println("Affine point A: " + point1.toString());
        System.out.println("Affine point B: " + point2.toString());

        ProjectivePoint point3 = AffinePoint.affineToProjective(point1);
        ProjectivePoint point4 = AffinePoint.affineToProjective(point2);

        System.out.println("\nProjectivePoint A: " + point3);
        System.out.println("ProjectivePoint B: " + point4);

        System.out.println("\nA add B: " + ProjectivePoint.pointAdd(point3,point4,a,p));

        System.out.println("\nDouble A: " + ProjectivePoint.pointDouble(point3,a,p));
        System.out.println("Double B: " + ProjectivePoint.pointDouble(point4,a,p));

        System.out.println("\nScalar Multiplication Montgomery A: " + ProjectivePoint.scalarMultiplicationM(point3,n,a,p));
        System.out.println("Scalar Multiplication Montgomery B: " + ProjectivePoint.scalarMultiplicationM(point4,n,a,p));

        ProjectivePoint point5 = ProjectivePoint.scalarMultiplicationM(point3,n,a,p);
        AffinePoint point6 = ProjectivePoint.projectToAffine(point5,p);
        System.out.println("\nProjective Point C: " + point5);
        System.out.println("Projective Point To Affine: "  + point6);


        System.out.println("\nThanks for your attention : ) ");
    }


}