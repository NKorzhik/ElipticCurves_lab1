package org.kpi;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.math.BigInteger;
import java.security.*;
import java.util.*;

public class AESUtil {

    final static Parameters par = new Parameters();
    final static Operations operations = new Operations();

    public ProjectivePoint getSharedKey(ProjectivePoint point, BigInteger d) {
        return operations.scalarMultiplication(point, d, par.a, par.p);
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        return keyGenerator.generateKey();
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec ivParameterSpec) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key,ivParameterSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key,IvParameterSpec ivParameterSpec) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key,ivParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }


    public static Map<String,BigInteger> digitalSignature(String message, ProjectivePoint point, BigInteger privateKey ) throws NoSuchAlgorithmException {

        Map<String,BigInteger> result = new HashMap<>();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        BigInteger r;
        BigInteger k;
        BigInteger h;

        do {
            byte[] h1 = md.digest(message.getBytes());
            h = new BigInteger(h1);
            k = operations.getRandomBigInteger(par.n);
            ProjectivePoint point2 = operations.scalarMultiplication(point, k, par.a, par.p);
            AffinePoint point3 = operations.projectToAffine(point2, par.p);
            r = point3.x().mod(par.n);
        } while (r.equals(BigInteger.ZERO));

        BigInteger s = k.modInverse(par.n).multiply(h.add(privateKey.multiply(r))).mod(par.n);
        result.put("s",s);
        result.put("r",r);

        return result;
    }

    public static boolean verification(String message,Map <String,BigInteger> map, ProjectivePoint P1, ProjectivePoint QA) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] h1 = md.digest(message.getBytes());
        BigInteger h = new BigInteger(h1);

        BigInteger u1 = map.get("s").modInverse(par.n).multiply(h).mod(par.n);
        BigInteger u2 = map.get("s").modInverse(par.n).multiply(map.get("r")).mod(par.n);

        ProjectivePoint res = operations.pointAdd(
                operations.scalarMultiplication(P1,u1,par.a,par.p),
                operations.scalarMultiplication(QA,u2,par.a,par.p),
                par.a,par.p);
        AffinePoint res1 = operations.projectToAffine(res, par.p);

        BigInteger v = res1.x().mod(par.n);

        return v.equals(map.get("r"));
    }

}
