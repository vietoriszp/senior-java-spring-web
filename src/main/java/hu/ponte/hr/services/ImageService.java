package hu.ponte.hr.services;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.dao.ImageRepository;
import hu.ponte.hr.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A képekkel kapcsolatos szolgáltatást végző osztály
 */
@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;


    /**
     * A teljes tárolt képhet zartozó metaadatok listáját szolgáltató metódus
     *
     * @return A teljes képekhez tartozó meataadat lista
     */
    public List<ImageMeta> getAllMetaDatas() {
        Iterable<Image> images = imageRepository.findAllMeteData();
        return convertImagesToMetas(images);
    }

    /**
     * A tárolt képadatokból kinyeri a metaadatokat
     *
     * @param images A lekérdezett kép
     * @return
     */
    private List<ImageMeta> convertImagesToMetas(Iterable<Image> images) {
        Objects.requireNonNull(images);
        return StreamSupport.stream(images.spliterator(), false).
                map(i -> new ImageMeta(i.getId().toString(), i.getName(), i.getMimeType(), i.getSize(), i.getDigitalSign())).
                collect(Collectors.toList());
    }

    /**
     * A kisméretű kép lekérést végző metódus
     *
     * @param id A kép azonosítója
     * @return Az azonosító alapján visszaadott kisméretű kép
     */
    public byte[] getThumbnailById(int id) {
        return imageRepository.findThumbnailById(id).getThumbnail();
    }
}
