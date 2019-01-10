package hu.ponte.hr.config;

import hu.ponte.common.upload.DefaultUploadService;
import hu.ponte.common.upload.SessionUploadContextRepository;
import hu.ponte.common.upload.UploadContextRepository;
import hu.ponte.common.upload.UploadController;
import hu.ponte.common.upload.binaryupload.UploadedFileTemporaryStore;
import hu.ponte.common.upload.binaryupload.sessionstore.SessionUploadedFileTemporaryStore;
import hu.ponte.common.upload.utils.JsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.nio.file.Paths;
import java.util.Locale;

/**
 * @author zoltan
 */
@Configuration
public class AppConfig
{
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(Locale.ENGLISH);
	}

	@Bean
	public UploadContextRepository uploadContextRepository() {
		return new SessionUploadContextRepository();
	}

	@Bean
	public UploadedFileTemporaryStore uploadedFileTemporaryStore() {
		return new SessionUploadedFileTemporaryStore("/var/tmp/korte");
	}

	@Bean
	public DefaultUploadService defaultUploadService(UploadContextRepository uploadContextRepository) {
		DefaultUploadService defaultUploadService = new DefaultUploadService("defaultUploadService", JsonUtils::toJson);
		defaultUploadService.setUploadContextRepository(uploadContextRepository);
		defaultUploadService.setMaxFileSize(10000000);
		defaultUploadService.setTempDirectory(Paths.get("/tmp/"));

		return defaultUploadService;
	}

	@Bean
	public UploadController uiUploadController() {
		return new UploadController();
	}
}
