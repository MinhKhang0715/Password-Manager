package org.example.passwordmanager.Crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESCrypto {
    private static AESCrypto aesInstance;
    private static final byte[] bytes = {89, 21, -28, -112, -7, 29, -84, -96, 85, -67, -48, -50, -3, 121, 121, 124};
    private static final IvParameterSpec iv = new IvParameterSpec(bytes);
    private final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    private static final String OS_NAME = System.getProperty("os.name");
    private static final String PATH = System.getProperty("user.home") + "/.secret/";
    private static final String PATH_IN_WINDOWS = System.getProperty("user.home") + File.separator + ".secret";
    private static final String FILE_NAME = "KEY.txt";
    private final SecretKey secretKey;

    public static AESCrypto getInstance() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if (aesInstance == null)
            aesInstance = new AESCrypto();
        return aesInstance;
    }

    private AESCrypto() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if (OS_NAME.equals("Linux")) {
            File file = new File(PATH + File.pathSeparator + FILE_NAME);
            if (!file.exists())
                createSecretKeyFile(PATH);
            this.secretKey = getKeyFromFile(PATH);
            System.out.println(this.secretKey);
        }
        else if (OS_NAME.contains("Windows")) {
            File file = new File(PATH_IN_WINDOWS + File.pathSeparator + FILE_NAME);
            if (!file.exists())
                createSecretKeyFile(PATH_IN_WINDOWS);
            this.secretKey = getKeyFromFile(PATH_IN_WINDOWS);
        }
        else {
            this.secretKey = null;
        }
    }

    private SecretKey generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createSecretKeyFile(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created) System.out.println("ERROR: fail to create directory");
        }
        File file = new File(path + File.pathSeparator + FILE_NAME);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            SecretKey key = generateKey();
            if (key != null) {
                byte[] keyAsBytes = key.getEncoded();
                outputStream.write(keyAsBytes);
            }
            else System.out.println("ERROR: fail to create a secret key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SecretKey getKeyFromFile(String path) {
        try (FileInputStream inputStream = new FileInputStream(path + File.pathSeparator + FILE_NAME)) {
            byte[] keyAsBytes = inputStream.readAllBytes();
            return new SecretKeySpec(keyAsBytes, 0, keyAsBytes.length, "AES");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encrypt(String plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, iv);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException |
                InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String cipherText) {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey, iv);
            return new String(this.cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (InvalidKeyException |
                IllegalBlockSizeException |
                BadPaddingException |
                InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
