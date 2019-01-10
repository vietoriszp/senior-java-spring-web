package hu.ponte.hr.services.sign;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author zoltan
 */
@Service
public class DefaultSignService implements SignService
{
	@Value("${sign.service.private.key}")
	private Path privKeyPath;

	@Value("${sign.service.public.key}")
	private Path publicKeyPath;

	@Autowired
	private ApplicationContext applicationContext;

	private byte[] privKeyBytes;
	private byte[] pubKeyBytes;

	@Override
	public String sign(InputStream stream)
	{
		try
		{
			byte[] bytes = IOUtils.toByteArray(stream);

			byte[] signed = signFile(bytes, privKeyBytes);

			return Base64.getEncoder().encodeToString(signed);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@PostConstruct
	private void init() throws Exception
	{
		try (InputStream in = applicationContext.getResource("classpath:" + privKeyPath).getInputStream())
		{
			this.privKeyBytes = IOUtils.toByteArray(in);
		}

		try (InputStream in = applicationContext.getResource("classpath:" + publicKeyPath).getInputStream())
		{
			this.pubKeyBytes = IOUtils.toByteArray(in);
		}

	}

	public static byte[] signFile(byte[] testFile, byte[] privKeyBytes) throws Exception
	{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec ks = new PKCS8EncodedKeySpec(privKeyBytes);
		PrivateKey privateKey = keyFactory.generatePrivate(ks);

		Signature signAlgo = Signature.getInstance("SHA256withRSA");
		signAlgo.initSign(privateKey);
		signAlgo.update(testFile);

		return signAlgo.sign();
	}

	public static boolean verifyFileSignature(byte[] testFile, byte[] signature, byte[] pubKeyBytes) throws Exception
	{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		KeySpec ks = new X509EncodedKeySpec(pubKeyBytes);
		PublicKey publicKey = keyFactory.generatePublic(ks);

		Signature signAlgo = Signature.getInstance("SHA256withRSA");
		signAlgo.initVerify(publicKey);
		signAlgo.update(testFile);

		return signAlgo.verify(signature);
	}
}
