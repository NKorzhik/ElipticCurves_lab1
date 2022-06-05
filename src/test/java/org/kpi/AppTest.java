package org.kpi;

import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.testng.Assert.assertEquals;


public class AppTest {
    private static Parameters par = new Parameters();
    private static Operations operations = new Operations();
    private static ProjectivePoint POINT_AT_INFINITY = new ProjectivePoint(BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO);

    @Test
    public void isSmooth() {
        assertEquals(operations.isSmooth(par.a, par.b, par.p), true);
    }

    @Test
    public void existPoint() {
        AffinePoint point = operations.createAffinePoint(par.a, par.b, par.p);
        assertEquals(operations.existPoint(point, par.a, par.b, par.p), true);
    }

    @Test
    public void sqrtMod() {
        // a = 4, p = 15 -> k = 3 -> y = 4^4 mod 15 == 1.
        assertEquals(operations.sqrtMod(BigInteger.valueOf(4), BigInteger.valueOf(15)), BigInteger.ONE);
    }

    @Test
    public void pointAdd() {
        // P1(6,1) P2(8,1) a = 0, p = 37 -> expected P3(23,36)
        ProjectivePoint A = operations.affineToProjective(new AffinePoint(BigInteger.valueOf(6), BigInteger.valueOf(1)));
        ProjectivePoint B = operations.affineToProjective(new AffinePoint(BigInteger.valueOf(8), BigInteger.valueOf(1)));
        AffinePoint C = operations.projectToAffine(operations.pointAdd(A, B, BigInteger.ZERO, BigInteger.valueOf(37)), BigInteger.valueOf(37));
        assertEquals(C, new AffinePoint(BigInteger.valueOf(23), BigInteger.valueOf(36)));
    }

    @Test
    public void pointDouble() {
        // P(5,11) a = 2, p = 17 -> 2P(15,5)
        ProjectivePoint D = operations.affineToProjective(new AffinePoint(BigInteger.valueOf(5), BigInteger.valueOf(11)));
        ProjectivePoint E = operations.pointDouble(D, BigInteger.valueOf(2), BigInteger.valueOf(17));
        AffinePoint res = operations.projectToAffine(E, BigInteger.valueOf(17));
        assertEquals(res, new AffinePoint(BigInteger.valueOf(15), BigInteger.valueOf(5)));

    }

    @Test
    public void scalarMultiplication_192() {
        AffinePoint point = operations.createAffinePoint(par.a, par.b, par.p);
        ProjectivePoint point1 = operations.affineToProjective(point);
        assertEquals(operations.scalarMultiplication(point1, par.n, par.a, par.p), POINT_AT_INFINITY);
    }

    @Test
    public void scalarMultiplicationM_192() {
        AffinePoint point = operations.createAffinePoint(par.a, par.b, par.p);
        ProjectivePoint point1 = operations.affineToProjective(point);
        assertEquals(operations.scalarMultiplicationM(point1, par.n, par.a, par.p), POINT_AT_INFINITY);
    }

}
