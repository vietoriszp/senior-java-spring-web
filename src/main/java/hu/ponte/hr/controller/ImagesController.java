package hu.ponte.hr.controller;

import hu.ponte.hr.services.store.ImageEntity;
import hu.ponte.hr.services.store.ImageStore;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("api/images")
public class ImagesController
{

	@Autowired
	private ImageStore imageStore;

	@GetMapping("meta")
	public List<ImageMeta> listImages()
	{
		return imageStore.findAll()
			.stream()
			.map(imageEntity -> ImageMeta.builder()
				.digitalSign(imageEntity.getDigitalSign())
				.id(imageEntity.getId())
				.size(imageEntity.getSize())
				.name(imageEntity.getImageName())
				.mimeType(imageEntity.getMimeType())
				.build())
			.collect(Collectors.toList());
	}

	@GetMapping("preview/{id}")
	public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IOException
	{
		ImageEntity imageEntity = imageStore.get(id);

		Path filePath = imageEntity.getFilePath();

		response.setContentType(imageEntity.getMimeType());
		try (InputStream inputStream = Files.newInputStream(filePath))
		{
			IOUtils.copy(inputStream, response.getOutputStream());
		}
	}

}
