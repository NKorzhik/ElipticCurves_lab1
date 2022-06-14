package org.kpi;

import java.math.BigInteger;

public class Parameters {

    Operations operations = new Operations();
    BigInteger a = BigInteger.valueOf(-3);
    BigInteger b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1",16);
    BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
    BigInteger n = new BigInteger("6277101735386680763835789423176059013767194773182842284081");

    //Parameters for ENC&DEC
    String message = "Tired Student";
    String algorithm = "AES/CBC/PKCS5Padding";

    final static ProjectivePoint POINT_AT_INFINITY = new ProjectivePoint(BigInteger.ZERO, BigInteger.ONE, BigInteger.ZERO);

}
