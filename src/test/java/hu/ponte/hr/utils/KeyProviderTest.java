package hu.ponte.hr.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource("classpath:keypath.properties")
public class KeyProviderTest {

    @Autowired
    private Environment env;

    /**
     * A privát kulcs beolvasásának és algokitmusának visszaellenőrzése.
     */
    @Test
    public void privateKey() {
        try {
            PrivateKey privateKey = KeyProvider.getPKCS8RSAKey(env.getProperty("PRIVATE_KEY"));
            Assert.assertNotNull(privateKey);
            Assert.assertEquals(privateKey != null ? privateKey.getAlgorithm() : "", "RSA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}