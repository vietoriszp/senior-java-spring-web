package hu.ponte.hr.services.store;

import java.nio.file.Path;
import java.util.List;

/**
 * @author zoltan
 */
public interface ImageStore
{
	List<ImageEntity> findAll();

	ImageEntity get(String id);

	ImageEntity save(String mimeType, long size, Path filePath, String imageName);
}
