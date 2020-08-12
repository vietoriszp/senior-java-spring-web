package hu.ponte.hr.dao;

import hu.ponte.hr.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * A képek tartós adatainak kezelését megvalósító interfész
 *
 * @author vpeter
 */
public interface ImageRepository extends JpaRepository<Image, Integer> {

    /**
     * Kisméretű kép lekérése azonosító alapján
     *
     * @param id A kép azonosítója
     * @return A kép azonosító által visszaadott kisméretű kép. Az Image csak a kisméretű méretű képet tartalmazza.
     */
    @Query(name = "Image.findThumbnailById", nativeQuery = true)
    Image findThumbnailById(@Param("id") Integer id);

    /**
     * A teljes méretű kép lekérdezése azonosító alapján
     *
     * @param id A kép azonosítója
     * @return A kép azonosító által visszaadott kép. Az Image csak a teljes méretű képet tartalmazza.
     */
    @Query(name = "Image.findImageById", nativeQuery = true)
    Image findImageById(@Param("id") Integer id);

    /**
     * A képhez tartozó metaadatok lekérése
     *
     * @return Az összes kép metaadat
     */
    @Query(name = "Image.findAllMeteData", nativeQuery = true)
    List<Image> findAllMeteData();

}
