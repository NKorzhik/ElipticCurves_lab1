package org.kpi;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;

public class App {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        Parameters par = new Parameters();
        Operations operations = new Operations();
        AESUtil system = new AESUtil();
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        //------------------------------------------Encrypted--------------------------------------------//

        //Parameters
        SecretKey key = AESUtil.generateKey(128);
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        AffinePoint point = operations.createAffinePoint(par.a, par.b, par.p);
        ProjectivePoint point1 = operations.affineToProjective(point);

        //Generation key for Bob
        BigInteger secretKeyBobDB = operations.getRandomBigInteger(par.n);
        ProjectivePoint publicKeyBobQB = system.getSharedKey(point1, secretKeyBobDB);

        //Encryption
        String C_M = AESUtil.encrypt(par.algorithm, par.message, key, ivParameterSpec);

        BigInteger secretKeyAlicaEA = operations.getRandomBigInteger(par.n);
        ProjectivePoint publicKeyAliceQA = system.getSharedKey(point1, secretKeyAlicaEA);

        //A shared secret according to the protocol of Diffie Gellman
        ProjectivePoint SAlica = system.getSharedKey(publicKeyBobQB, secretKeyAlicaEA);
        AffinePoint SAlicaAffine = operations.projectToAffine(SAlica, par.p);


        byte[] Sx = md.digest(SAlicaAffine.x().toByteArray());

        //WRAP
        // Secret key to bigInteger + byte[] s to Biginteger
        BigInteger keyTo = new BigInteger(key.getEncoded());
        BigInteger ASxTo = new BigInteger(Sx);
        BigInteger C_k = keyTo.xor(ASxTo);

        //------------------------------------------Decrypted--------------------------------------------//
        // Get publicKeyAlicaQA, C_k , C_M

        //A shared secret according to the protocol of Diffie Gellman
        ProjectivePoint SBob = system.getSharedKey(publicKeyAliceQA, secretKeyBobDB);
        AffinePoint SBobAffine = operations.projectToAffine(SBob, par.p);

        //UNWRAP
        byte[] BSx = md.digest(SBobAffine.x().toByteArray());
        BigInteger k = C_k.xor(new BigInteger(BSx));

        //Decryption
        String M = AESUtil.decrypt(par.algorithm, C_M, new SecretKeySpec(k.toByteArray(), "AES"), ivParameterSpec);

        System.out.println("Equals? " + M.equals(par.message));

        //---------------------------------digital signature---------------------------------//

        AffinePoint P = operations.createAffinePoint(par.a,par.b,par.p);
        ProjectivePoint P1 = operations.affineToProjective(P);
        BigInteger privateKey = operations.getRandomBigInteger(par.n);
        ProjectivePoint QA = operations.scalarMultiplication(P1,privateKey,par.a,par.p);
        byte[] h = md.digest(par.message.getBytes());
        BigInteger h1 = new BigInteger(h);

        BigInteger k2 = operations.getRandomBigInteger(par.n);

        ProjectivePoint point2 = operations.scalarMultiplication(P1,k2,
                par.a,par.p);
        AffinePoint point3 = operations.projectToAffine(point2,par.p);

        BigInteger r = point3.x().mod(par.n);

        BigInteger s = k2.modInverse(par.n).multiply(h1.add(privateKey.multiply(r))).mod(par.n);

        //----------------------verification------------------------//
        //get message (r,s) publicKeyAliceQA

        BigInteger u1 = s.modInverse(par.n).multiply(h1).mod(par.n);
        BigInteger u2 = s.modInverse(par.n).multiply(r).mod(par.n);

        ProjectivePoint res = operations.pointAdd(
                operations.scalarMultiplication(P1,u1,par.a,par.p),
                operations.scalarMultiplication(QA,u2,par.a,par.p),
                par.a,par.p);

        AffinePoint res1 = operations.projectToAffine(res, par.p);

        BigInteger v = res1.x().mod(par.n);

        System.out.println("Equals? " + v.equals(r));


    }

}