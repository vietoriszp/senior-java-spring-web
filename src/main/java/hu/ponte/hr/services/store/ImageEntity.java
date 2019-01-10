package hu.ponte.hr.services.store;

import lombok.Builder;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Date;

/**
 * @author zoltan
 */
@Getter
@Builder
public class ImageEntity
{
	private String id;
	private String imageName;
	private String mimeType;
	private long size;
	private String digitalSign;
	private Date created;
	private Path filePath;
}
