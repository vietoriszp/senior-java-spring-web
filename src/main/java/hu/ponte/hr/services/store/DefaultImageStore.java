package hu.ponte.hr.services.store;

import hu.ponte.hr.services.sign.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zoltan
 */
@Service
public class DefaultImageStore implements ImageStore
{
	@Value("${image.store.base.folder}")
	private Path basePath;

	@Autowired
	private SignService signService;

	private Map<String, ImageEntity> db = new ConcurrentHashMap<>();

	@PostConstruct
	private void init() throws IOException
	{
		if (!Files.exists(basePath))
		{
			Files.createDirectories(basePath);
		}
	}

	@Override
	public List<ImageEntity> findAll()
	{
		return new ArrayList<>(db.values());
	}

	@Override
	public ImageEntity get(String id)
	{
		return db.get(id);
	}

	@Override
	public ImageEntity save(String mimeType, long size, Path filePath, String imageName)
	{
		try
		{
			String newId = String.valueOf(newId());

			Path fileStoredPath = copyFile(filePath, newId);
			String digitalSign = createDigitalSign(fileStoredPath);

			ImageEntity imageEntity = ImageEntity.builder()
				.id(newId)
				.created(new Date())
				.imageName(imageName)
				.mimeType(mimeType)
				.size(size)
				.digitalSign(digitalSign)
				.filePath(fileStoredPath)
				.build();

			db.put(imageEntity.getId(), imageEntity);

			return imageEntity;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

	}

	private int newId() {
		Random r = new Random();
		int low = 10;
		int high = 100000000;
		return r.nextInt(high-low) + low;
	}

	private Path copyFile(Path filePath, String newId) throws IOException
	{
		Path fileStoredPath = basePath.resolve(newId);
		Files.copy(filePath, fileStoredPath);
		return fileStoredPath;
	}

	private String createDigitalSign(Path filePath)
	{
		try(InputStream stream = Files.newInputStream(filePath))
		{
			return signService.sign(stream);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
