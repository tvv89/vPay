package com.tvv.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash functions for password
 */
public class StringHash {

    /**
     * Create new Hash string
     * @param input input string
     * @return hash string
     */
    public static String getHashString(String input) {
        String outputHash = "";
        try {
            /**
             * use SHA-256
             */
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, encodedHash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32)
            {
                hexString.insert(0, '0');
            }
            outputHash =  hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return outputHash;
    }

    public static boolean equalsHash (String input, String hashString){
        return hashString.equals(getHashString(input));
    }

}
