package hu.ponte.hr.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Maximális fájlméret túllépésének kezelése.
 */
@ControllerAdvice
public class FileUploadExceptionAdvice {

    @Autowired
    private Environment env;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(
            MaxUploadSizeExceededException exc,
            HttpServletRequest request,
            HttpServletResponse response) {

        ModelAndView modelAndView = new ModelAndView("redirect:/error.html");
        modelAndView.getModel().put("file_size_error_msg", String.format(env.getProperty("FILE_SIZE_ERROR"), Math.round(exc.getMaxUploadSize() / 1048576)));
        return modelAndView;
    }
}