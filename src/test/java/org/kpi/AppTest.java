package org.kpi;

import org.testng.annotations.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class AppTest {
    private static final Parameters par = new Parameters();
    private static final Operations operations = new Operations();

    private static final AESUtil system = new AESUtil();

    private static final ProjectivePoint POINT_AT_INFINITY = new ProjectivePoint(BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO);

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

    @Test
    public void keyExchange(){
        AffinePoint point = operations.createAffinePoint(par.a, par.b, par.p);
        ProjectivePoint point1 = operations.affineToProjective(point);

        BigInteger dA = operations.getRandomBigInteger(par.p);
        BigInteger dB = operations.getRandomBigInteger(par.p);

        ProjectivePoint QA = system.getSharedKey(point1,dA);
        ProjectivePoint QB = system.getSharedKey(point1,dB);

        ProjectivePoint SB = system.getSharedKey(QA,dB);
        ProjectivePoint SA = system.getSharedKey(QB,dA);

        AffinePoint SA_final = operations.projectToAffine(SA,par.p);
        AffinePoint SB_final = operations.projectToAffine(SB,par.p);
        assertEquals(SA_final,SB_final);

    }

    @Test
    void givenString_whenEncrypt_thenSuccess()
            throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        String input = "baeldung";
        SecretKey key = AESUtil.generateKey(128);
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";
        String cipherText = AESUtil.encrypt(algorithm, input, key, ivParameterSpec);
        String plainText = AESUtil.decrypt(algorithm, cipherText, key, ivParameterSpec);
        assertEquals(input, plainText);
    }

    @Test
    void signature() throws NoSuchAlgorithmException {
        AffinePoint P = operations.createAffinePoint(par.a,par.b,par.p);
        ProjectivePoint P1 = operations.affineToProjective(P);
        BigInteger privateKey = operations.getRandomBigInteger(par.n);
        ProjectivePoint QA = operations.scalarMultiplication(P1,privateKey,par.a,par.p);

        Map<String,BigInteger> dig = AESUtil.digitalSignature(par.message,P1,privateKey);

        assertTrue(AESUtil.verification(par.message, dig, P1, QA));

    }

}
