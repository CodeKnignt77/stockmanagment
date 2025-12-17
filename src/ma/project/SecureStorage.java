package ma.project;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.util.Base64;

public class SecureStorage {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public SecureStorage(PublicKey pub, PrivateKey priv) {
        this.publicKey = pub;
        this.privateKey = priv;
    }

    // Chiffre une donnée sensible
    public String encrypt(String plainText) throws Exception {
        // 1. Clé AES aléatoire
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey aesKey = keyGen.generateKey();

        // 2. Chiffrement AES de la donnée
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedData = aesCipher.doFinal(plainText.getBytes());

        // 3. Chiffrement de la clé AES avec RSA public
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        // 4. Format : donnée|cléAES (Base64)
        return Base64.getEncoder().encodeToString(encryptedData) + "|" +
               Base64.getEncoder().encodeToString(encryptedAesKey);
    }

    // Déchiffre une donnée sensible
    public String decrypt(String cipherText) throws Exception {
        String[] parts = cipherText.split("\\|");
        byte[] encryptedData = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedAesKey = Base64.getDecoder().decode(parts[1]);

        // 1. Déchiffrement clé AES avec RSA privé
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
        SecretKey aesKey = new javax.crypto.spec.SecretKeySpec(aesKeyBytes, "AES");

        // 2. Déchiffrement donnée avec AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(aesCipher.doFinal(encryptedData));
    }
}