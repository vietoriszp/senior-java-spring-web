package hu.ponte.hr.upload;

import hu.ponte.common.upload.UploadContext;
import hu.ponte.common.upload.binaryupload.AbstractBinaryFileUploadHandler;
import hu.ponte.hr.services.store.ImageStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zoltan
 */
@Component("ImageUploadHandler")
public class ImageUploadHandler extends AbstractBinaryFileUploadHandler
{

	@Autowired
	private ImageStore imageStore;

	@Override
	protected void validateUploadedFile(UploadContext uploadContext)
	{
		if (!uploadContext.getFileType().startsWith("image/"))
		{
			throw new RuntimeException("wrong.image.type");
		}

		if (uploadContext.getTotal()> 1000000)
		{
			throw new RuntimeException("max.file.size.reached");
		}
	}

	@Override
	public void onDone(UploadContext context)
	{
		imageStore.save(context.getFileType(), context.getTotal(), context.getFilePath(), context.getFileName());

		super.onDone(context);
	}
}
