package ma.project;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class RSAUtil {

    private BigInteger n, e, d;

    public RSAUtil(String password) {
        try {
            // Dérivation de seed déterministe à partir du mot de passe
            char[] passChars = password.toCharArray();
            byte[] salt = "StockSecureSalt2025".getBytes(); // salt fixe pour reproductibilité
            PBEKeySpec spec = new PBEKeySpec(passChars, salt, 10000, 256);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] seed = skf.generateSecret(spec).getEncoded();

            SecureRandom random = new SecureRandom(seed);

            // Génération RSA 2048-bits
            BigInteger p = BigInteger.probablePrime(1024, random);
            BigInteger q = BigInteger.probablePrime(1024, random);
            n = p.multiply(q);
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            e = BigInteger.valueOf(65537);
            d = e.modInverse(phi);
        } catch (Exception ex) {
            throw new RuntimeException("Erreur génération clé RSA");
        }
    }

    public String encrypt(String plainText) {
        byte[] bytes = plainText.getBytes();
        BigInteger m = new BigInteger(1, bytes);
        BigInteger c = m.modPow(e, n);
        return Base64.getEncoder().encodeToString(c.toByteArray());
    }

    public String decrypt(String cipherText) {
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        BigInteger c = new BigInteger(bytes);
        BigInteger m = c.modPow(d, n);
        return new String(m.toByteArray()).trim();
    }
}