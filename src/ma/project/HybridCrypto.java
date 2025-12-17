package ma.project;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

public class HybridCrypto {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    // Constructeur : charge les clés RSA depuis rsa_key.txt
    public HybridCrypto() throws Exception {
        String[] keys = loadRSAKeysFromFile();
        privateKey = loadPrivateKey(keys[0]);
        publicKey = loadPublicKey(keys[1]);
    }

    // Charge les clés depuis rsa_key.txt
    private String[] loadRSAKeysFromFile() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("rsa_key.txt"))) {
            String privateKeyBase64 = br.readLine();
            String publicKeyBase64 = br.readLine();
            return new String[]{privateKeyBase64, publicKeyBase64};
        }
    }

    private PrivateKey loadPrivateKey(String base64) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(base64);
        BigInteger d = new BigInteger(bytes);
        BigInteger n = new BigInteger(Base64.getDecoder().decode(loadRSAKeysFromFile()[1].split("\\|")[0])); // on récupère n depuis public
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new java.security.spec.RSAPrivateKeySpec(n, d));
    }

    private PublicKey loadPublicKey(String base64) throws Exception {
        String[] parts = base64.split("\\|");
        byte[] nBytes = Base64.getDecoder().decode(parts[0]);
        byte[] eBytes = Base64.getDecoder().decode(parts[1]);
        BigInteger n = new BigInteger(nBytes);
        BigInteger e = new BigInteger(eBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new java.security.spec.RSAPublicKeySpec(n, e));
    }

    // Chiffre une donnée sensible
    public String encrypt(String plainText) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey aesKey = keyGen.generateKey();

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedData = aesCipher.doFinal(plainText.getBytes());

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        return Base64.getEncoder().encodeToString(encryptedData) + "|" +
               Base64.getEncoder().encodeToString(encryptedAesKey);
    }

    // Déchiffre une donnée sensible
    public String decrypt(String cipherText) throws Exception {
        String[] parts = cipherText.split("\\|");
        byte[] encryptedData = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedAesKey = Base64.getDecoder().decode(parts[1]);

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
        SecretKey aesKey = new javax.crypto.spec.SecretKeySpec(aesKeyBytes, "AES");

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(aesCipher.doFinal(encryptedData));
    }
}