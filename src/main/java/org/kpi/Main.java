package org.kpi;

import java.awt.*;
import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        // P - 192

        BigInteger a = BigInteger.valueOf(-3);
        BigInteger b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1",16);
        BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        BigInteger n = new BigInteger("6277101735386680763835789423176059013767194773182842284081");

        Operations operations = new Operations();

        System.out.println(operations.isSmooth(a,b,p));

        AffinePoint point1 = operations.createAffinePoint(a,b,p);
        AffinePoint point2 = operations.createAffinePoint(a,b,p);

//
//        // test for sqrtMod correct
//        System.out.println(operations.sqrtMod(BigInteger.valueOf(4), BigInteger.valueOf(15)));
//
//        // test for PointAdd correct
//        ProjectivePoint A = operations.affineToProjective(new AffinePoint(BigInteger.valueOf(6),BigInteger.valueOf(1)));
//        ProjectivePoint B = operations.affineToProjective(new AffinePoint(BigInteger.valueOf(8),BigInteger.valueOf(1)));
//        ProjectivePoint C = operations.pointAdd(A,B,BigInteger.valueOf(0),BigInteger.valueOf(37));
//        System.out.println(operations.projectToAffine(C,BigInteger.valueOf(37)));
//
//        // test for PointDouble correct
//        ProjectivePoint D = operations.affineToProjective(new AffinePoint(BigInteger.valueOf(5),BigInteger.valueOf(11)));
//        ProjectivePoint E = operations.pointDouble(D,BigInteger.valueOf(2),BigInteger.valueOf(17));
//        System.out.println(operations.projectToAffine(E,BigInteger.valueOf(17)));

        // test for PointMultiplication incorrect
        ProjectivePoint F = operations.affineToProjective(new AffinePoint(BigInteger.valueOf(16),BigInteger.valueOf(5)));
//        ProjectivePoint G = operations.scalarMultiplication(F,BigInteger.valueOf(2),BigInteger.valueOf(9),BigInteger.valueOf(23));
//        System.out.println(operations.projectToAffine(G,BigInteger.valueOf(23)));
        for (int i = 2; i < 10; i++) {
            ProjectivePoint G = operations.scalarMultiplication(F,BigInteger.valueOf(i),BigInteger.valueOf(9),BigInteger.valueOf(23));
            System.out.println(operations.projectToAffine(G,BigInteger.valueOf(23)));
        }


         //test for PointMultiplicationMontgomeri incorrect
//        for (int i = 2; i <10 ; i++) {
//            ProjectivePoint J = operations.scalarMultiplicationM(F,BigInteger.valueOf(i),BigInteger.valueOf(9),BigInteger.valueOf(23));
//            System.out.println(operations.projectToAffine(J,BigInteger.valueOf(23)));
//        }


        System.out.println("Affine point A: " + point1.toString());
        System.out.println("Affine point B: " + point2.toString());

        ProjectivePoint point3 = operations.affineToProjective(point1);
        System.out.println("\nProjectivePoint A: " + point3);
        ProjectivePoint point4 = operations.affineToProjective(point2);
        System.out.println("ProjectivePoint B: " + point4);


        System.out.println(operations.pointDouble(point3,a,p));
        System.out.println(operations.pointDouble(point4,a,p));


        System.out.println(operations.pointAdd(point3,point4,a,p));
        System.out.println(operations.pointAdd(point4,point3,a,p));

        System.out.println("----");
        System.out.println(operations.scalarMultiplication(point3,n,a,p));
        System.out.println(operations.scalarMultiplication(point4,n,a,p));
        System.out.println("----");
        System.out.println(operations.scalarMultiplicationM(point3,n,a,p));
        System.out.println(operations.scalarMultiplicationM(point4,n,a,p));



    }


}