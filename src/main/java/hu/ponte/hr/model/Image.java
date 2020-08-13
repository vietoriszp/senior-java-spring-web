package hu.ponte.hr.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

/**
 * A teljesítmény fokozása érdekében csak a szükséges adatotkell felolvasni.
 */
@SqlResultSetMappings({
        @SqlResultSetMapping(name = "Image.thumbnail",
                classes = @ConstructorResult(targetClass = Image.class,
                        columns = {
                                @ColumnResult(name = "THUMBNAIL", type = byte[].class)
                        })
        ),
        @SqlResultSetMapping(name = "Image.image",
                classes = @ConstructorResult(targetClass = Image.class,
                        columns = {
                                @ColumnResult(name = "IMAGE", type = byte[].class)
                        })
        ),
        @SqlResultSetMapping(name = "Image.metadata",
                classes = @ConstructorResult(targetClass = Image.class,
                        columns = {
                                @ColumnResult(name = "ID", type = Integer.class),
                                @ColumnResult(name = "NAME", type = String.class),
                                @ColumnResult(name = "MIME_TYPE", type = String.class),
                                @ColumnResult(name = "SIZE", type = Long.class),
                                @ColumnResult(name = "DIGITAL_SIGN", type = String.class),
                        })
        )
})

/**
 * Az Image repository által használt lekérdezések.
 * @see hu.ponte.hr.dao.ImageRepository
 */
@NamedNativeQueries({
        @NamedNativeQuery(name = "Image.findThumbnailById", query = "SELECT i.THUMBNAIL FROM IMAGE i WHERE i.id = :id", resultSetMapping = "Image.thumbnail"),
        @NamedNativeQuery(name = "Image.findImageById", query = "SELECT i.IMAGE FROM IMAGE i WHERE i.id = :id", resultSetMapping = "Image.image"),
        @NamedNativeQuery(name = "Image.findAllMeteData", query = "SELECT i.ID, i.NAME, i.MIME_TYPE, i.SIZE, i.DIGITAL_SIGN FROM IMAGE i", resultSetMapping = "Image.metadata")
})

@Getter
@Setter
@Entity
@Table
public class Image {

    @Id
    @GeneratedValue
    Integer id;

    @Column
    private String name;

    @Column(name = "MIME_TYPE", length = 30)
    private String mimeType;

    @Column
    private long size;

    @Column
    @Lob
    private byte[] thumbnail;

    @Column
    @Lob
    private byte[] image;

    @Column(name = "DIGITAL_SIGN", length = 512)
    private String digitalSign;

    public Image() {
    }

    public Image(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Image(Integer id, String name, String mimeType, long size, String digitalSign) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.size = size;
        this.digitalSign = digitalSign;
    }
}
