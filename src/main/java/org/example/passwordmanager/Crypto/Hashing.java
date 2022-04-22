package org.example.passwordmanager.Crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hashing {

    public static String SHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA256");
        byte[] encodedBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encodedBytes);
    }

    public static byte[] MD5(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(input.getBytes(StandardCharsets.UTF_8));
        return messageDigest.digest();
    }
}
