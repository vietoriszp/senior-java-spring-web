package hu.ponte.hr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * A kulcsok ellátásáért felelős osztály
 *
 * @author vpeter
 */
public class KeyProvider {

    /**
     * @param resourceName A kulcsot tartalmazó állomány átalakítása privát kulcsobjektummá.
     * @return A privát kulcsobjektum
     * @see PrivateKey
     * @see NoSuchAlgorithmException
     * @see InvalidKeySpecException
     */
    public static PrivateKey getPKCS8RSAKey(String resourceName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream keyIS = KeyProvider.class.getResourceAsStream(resourceName);
        byte[] keyBytes = keyIS.readAllBytes();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        // A beolvasott PKCS8 kódolt fájlt alakítja át kulcsobjektummá RSA titkosító algoritmus használatával
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * @param resourceName A kulcsot tartalmazó állomány átalakítása publikus kulcsobjektummá.
     * @return A publikus kulcsobjektum
     * @see PrivateKey
     * @see NoSuchAlgorithmException
     * @see InvalidKeySpecException
     */
    public static PublicKey getX509RSAKey(String resourceName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream keyIS = KeyProvider.class.getResourceAsStream(resourceName);
        byte[] keyBytes = keyIS.readAllBytes();

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        // A beolvasott X509-es tanúsítványt alakítja át kulcsobjektummá RSA titkosító algoritmus használatával
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
