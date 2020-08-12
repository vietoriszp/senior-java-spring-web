package hu.ponte.hr;

import hu.ponte.hr.services.SignService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zoltan
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class SignTest {
    private final static String CAT = "cat.jpg";
    private final static String ENHANCED_BUZZ = "enhanced-buzz.jpg";
    private final static String RND = "rnd.jpg";

    @Autowired
    private SignService signService;

    Map<String, String> files = new LinkedHashMap<>() {
        {
            put("cat.jpg", "XYZ+wXKNd3Hpnjxy4vIbBQVD7q7i0t0r9tzpmf1KmyZAEUvpfV8AKQlL7us66rvd6eBzFlSaq5HGVZX2DYTxX1C5fJlh3T3QkVn2zKOfPHDWWItdXkrccCHVR5HFrpGuLGk7j7XKORIIM+DwZKqymHYzehRvDpqCGgZ2L1Q6C6wjuV4drdOTHps63XW6RHNsU18wHydqetJT6ovh0a8Zul9yvAyZeE4HW7cPOkFCgll5EZYZz2iH5Sw1NBNhDNwN2KOxrM4BXNUkz9TMeekjqdOyyWvCqVmr5EgssJe7FAwcYEzznZV96LDkiYQdnBTO8jjN25wlnINvPrgx9dN/Xg==");
            put("enhanced-buzz.jpg", "tsLUqkUtzqgeDMuXJMt1iRCgbiVw13FlsBm2LdX2PftvnlWorqxuVcmT0QRKenFMh90kelxXnTuTVOStU8eHRLS3P1qOLH6VYpzCGEJFQ3S2683gCmxq3qc0zr5kZV2VcgKWm+wKeMENyprr8HNZhLPygtmzXeN9u6BpwUO9sKj7ImBvvv/qZ/Tht3hPbm5SrDK4XG7G0LVK9B8zpweXT/lT8pqqpYx4/h7DyE+L5bNHbtkvcu2DojgJ/pNg9OG+vTt/DfK7LFgCjody4SvZhSbLqp98IAaxS9BT6n0Ozjk4rR1l75QP5lbJbpQ9ThAebXQo+Be4QEYV/YXf07WXTQ==");
            put("rnd.jpg", "lM6498PalvcrnZkw4RI+dWceIoDXuczi/3nckACYa8k+KGjYlwQCi1bqA8h7wgtlP3HFY37cA81ST9I0X7ik86jyAqhhc7twnMUzwE/+y8RC9Xsz/caktmdA/8h+MlPNTjejomiqGDjTGvLxN9gu4qnYniZ5t270ZbLD2XZbuTvUAgna8Cz4MvdGTmE3MNIA5iavI1p+1cAN+O10hKwxoVcdZ2M3f7/m9LYlqEJgMnaKyI/X3m9mW0En/ac9fqfGWrxAhbhQDUB0GVEl7WBF/5ODvpYKujHmBAA0ProIlqA3FjLTLJ0LGHXyDgrgDfIG/EDHVUQSdLWsM107Cg6hQg==");
        }
    };

    /**
     * Képaláírás visszaellenőrzése a files map cat.jpg kulcs alapján kapott signature alapján.
     */
    @Test
    public void test_Cat() {
        byte[] signedImage = signService.signImage(SignTest.class.getResourceAsStream("/images/" + CAT));
        Assert.assertEquals(files.get(CAT), new String(signedImage));
    }

    /**
     * Képaláírás visszaellenőrzése a files map enhanced-buzz.jpg kulcs alapján kapott signature alapján.
     */
//    @Test
    public void test_EnhancedBuzz() {
        byte[] signedImage = signService.signImage(SignTest.class.getResourceAsStream("/images/" + ENHANCED_BUZZ));
        Assert.assertEquals(files.get(ENHANCED_BUZZ), new String(signedImage));
    }

    /**
     * Képaláírás visszaellenőrzése a files map rnd.jpg kulcs alapján kapott signature alapján.
     */
    //   @Test
    public void test_Rnd() {
        byte[] signedImage = signService.signImage(SignTest.class.getResourceAsStream("/images/" + RND));
        Assert.assertEquals(files.get(RND), new String(signedImage));
    }

    /**
     * A képaláírás visszaellenőrzése
     *
     * @see IOException
     * @see SignatureException
     */
    @Test
    public void test_Verify() throws IOException {
        InputStream imageStream = SignTest.class.getResourceAsStream("/images/" + CAT);
        byte[] originImage = imageStream.readAllBytes();
        byte[] signedImage = signService.signImage(new ByteArrayInputStream(originImage));
        Assert.assertTrue(signService.verifyImage(new ByteArrayInputStream(originImage), signedImage));
    }
}
