package hu.ponte.hr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author zoltan
 */
@Configuration
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.ENGLISH);
    }

    /**
     * Fájlfeltöltés 2 MB-ra korlátozása
     *
     * @return
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(2097152);
        return multipartResolver;
    }

    /**
     * Maximális fájlméret túllépésének kezelése.
     */
    @ControllerAdvice
    public class FileUploadExceptionAdvice {

        @ExceptionHandler(MaxUploadSizeExceededException.class)
        public ModelAndView handleMaxSizeException(
                MaxUploadSizeExceededException exc,
                HttpServletRequest request,
                HttpServletResponse response) {

            ModelAndView modelAndView = new ModelAndView("file");
            modelAndView.getModel().put("dz-message", String.format(env.getProperty("FILE_SIZE_ERROR"), 2));
            return modelAndView;
        }
    }

}
