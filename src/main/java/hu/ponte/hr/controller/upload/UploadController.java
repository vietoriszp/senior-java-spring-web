package hu.ponte.hr.controller.upload;

import hu.ponte.hr.dao.ImageRepository;
import hu.ponte.hr.model.Image;
import hu.ponte.hr.services.SignService;
import hu.ponte.hr.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
@RequestMapping("api/file")
public class UploadController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private SignService signService;

    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) {
        Image image = convertFileToImage(file);
        imageRepository.save(image);
        return "ok";
    }

    /**
     * A feltöltött fájlt átkonvertálja Image entitássá
     *
     * @param file A feltöltött fájl
     * @return A konvertált Image entitás
     */
    private Image convertFileToImage(MultipartFile file) {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setMimeType(file.getContentType());
        image.setSize(file.getSize());
        try {
            byte[] imageBytes = getBytesFromFile(file);
            image.setImage(imageBytes);
            image.setThumbnail(ImageUtils.rescaleImage(imageBytes, 150, 100));
            image.setDigitalSign(new String(signService.signImage(file.getInputStream())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * A feltöltött fájl byte tömbé konvertálása
     *
     * @param file
     * @return
     * @throws IOException
     */
    private byte[] getBytesFromFile(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        return is.readAllBytes();
    }

}
