package hu.ponte.hr.controller;


import hu.ponte.hr.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author vpeter
 */
@RestController()
@RequestMapping("api/images")
public class ImagesController {

    @Autowired
    private ImageService imageService;

    /**
     * A kép metaadatok szoltálataását végző végpont
     *
     * @return A teljes tárolt képekhez tartozó metaadat lista
     */
    @GetMapping("meta")
    public List<ImageMeta> listImages() {
        return imageService.getAllMetaDatas();
    }

    /**
     * A kisméretű képek szolháltatását végző végpont
     *
     * @param id A paraméterként kapott képazonosító
     * @see HttpServletResponse
     */
    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) {
        try {
            ServletOutputStream os = response.getOutputStream();
            os.write(imageService.getThumbnailById(Integer.parseInt(id)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
