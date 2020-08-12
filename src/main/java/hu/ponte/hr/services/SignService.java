package hu.ponte.hr.services;

import hu.ponte.hr.utils.KeyProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

@Service
@Slf4j
@PropertySource({"classpath:keypath.properties", "classpath:messages.properties"})
public class SignService {

    private static final String SHA256_RSA = "SHA256withRSA";

    @Autowired
    private Environment env;

    /**
     * Egy kép aláírását végzi.
     *
     * @param image A kép adatfolyama
     * @return A kép signatúrája
     */
    public byte[] signImage(InputStream image) {
        BufferedInputStream bis = new BufferedInputStream(image);
        byte[] signatureBytes = null;
        try {
            byte[] content = bis.readAllBytes();
            Signature signature = Signature.getInstance(SHA256_RSA);
            signature.initSign(KeyProvider.getPKCS8RSAKey(env.getProperty("PRIVATE_KEY")));
            signature.update(content);
            signatureBytes = Base64.getEncoder().encode(signature.sign());
            if (!verifyImage(new ByteArrayInputStream(content), signatureBytes))
                log.error(env.getProperty("VERIFY_ERROR"));
        } catch (NoSuchAlgorithmException e) {
            log.error(env.getProperty("ALGORITHM_ERROR"));
        } catch (InvalidKeySpecException | InvalidKeyException e) {
            log.error(env.getProperty("KEY_ERROR"));
        } catch (SignatureException e) {
            log.error(env.getProperty("SIGNATURE_ERROR"));
        } catch (IOException e) {
            log.error(env.getProperty("IO_ERROR"), env.getProperty("OPEN_ERROR"));
        }
        Objects.requireNonNull(signatureBytes);
        return signatureBytes;
    }

    /**
     * A képaláírás visszaellenőrzése.
     *
     * @param image          Az eredeti képfolyam
     * @param signatureBytes Az aláírás
     * @return Igaz, ha sikeres a visszaellenőrzés
     */
    public boolean verifyImage(InputStream image, byte[] signatureBytes) {
        try {
            BufferedInputStream bis = new BufferedInputStream(image);
            byte[] content = bis.readAllBytes();
            Signature sign = Signature.getInstance(SHA256_RSA);
            //           sign.initVerify(KeyProvider.getX509RSAKey(provider.getValue("PUBLIC_KEY")));
            sign.initVerify(KeyProvider.getX509RSAKey(env.getProperty("PUBLIC_KEY")));
            sign.update(content);
            return sign.verify(Base64.getDecoder().decode(signatureBytes));
        } catch (NoSuchAlgorithmException e) {
            log.error(env.getProperty("ALGORITHM_ERROR"));
        } catch (InvalidKeySpecException | InvalidKeyException e) {
            log.error(env.getProperty("KEY_ERROR"));
        } catch (SignatureException e) {
            log.error(env.getProperty("SIGNATURE_ERROR"));
        } catch (IOException e) {
            log.error(env.getProperty("IOEXCEPTION"), env.getProperty("OPEN_ERROR"));
        }
        return false;
    }
}
