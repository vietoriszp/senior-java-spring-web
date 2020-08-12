package hu.ponte.hr.utils;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Képek kezelésére, konvertálására szolgáló osztály
 */
public class ImageUtils {


    /**
     * Kép konvertálása kisméretű képpé
     *
     * @param originalImage Az eredeti kép
     * @param with          A kép szélessége
     * @param height        A kép magassága
     * @return Az átméretezett kép
     * @see IOException
     */
    public static byte[] rescaleImage(byte[] originalImage, int with, int height) throws IOException {
        BufferedImage resizedImage = new BufferedImage(with, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(ImageIO.read(new ByteArrayInputStream(originalImage)), 0, 0, with, height, null);
        graphics2D.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        return baos.toByteArray();
    }
}
