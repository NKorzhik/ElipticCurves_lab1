package org.kpi;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        // P - 192

//        BigInteger p = new BigInteger("fffffffffffffffffffffffffffffffeffffffffffffffff",16);
//        BigInteger a = new BigInteger("fffffffffffffffffffffffffffffffefffffffffffffffc", 16);
//        BigInteger b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
//        BigInteger n = new BigInteger("ffffffffffffffffffffffff99def836146bc9b1b4d22831",16);

        BigInteger a = BigInteger.valueOf(-3);
        BigInteger b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1",16);
        BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
        BigInteger n = new BigInteger("6277101735386680763835789423176059013767194773182842284081");

        Operations operations = new Operations();

        System.out.println(operations.isSmooth(a,b,p));

        AffinePoint point1 = operations.createAffinePoint(a,b,p);
        AffinePoint point2 = operations.createAffinePoint(a,b,p);

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


        System.out.println(operations.scalarMultiplicationM(point3,n,a,p));
        System.out.println(operations.scalarMultiplicationM(point4,n,a,p));

        ProjectivePoint A = operations.scalarMultiplicationM(point3,n,a,p);
        System.out.println(operations.projectToAffine(A,p));
    }


}