package hu.ponte.hr.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * A megjelenítendő kép metaadatokat tartalmató osztály.
 *
 * @author zoltan
 * @author vpeter
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ImageMeta {
    private String id;
    private String name;
    private String mimeType;
    private long size;
    private String digitalSign;

}
