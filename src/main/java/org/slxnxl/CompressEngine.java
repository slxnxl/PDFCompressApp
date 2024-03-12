package org.slxnxl;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.PDPageTree;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CompressEngine {

    public static void compressImages(PDDocument document, float imageQuality) throws IOException {
        PDPageTree pages = document.getPages();
        for (PDPage page : pages) {
            compressImagesInPage(document, page, imageQuality);
        }

//        document.save("compressed_document.pdf", new CompressParameters(500));
//        document.close();
    }

    private static void compressImagesInPage(PDDocument document, PDPage page, float imageQuality) throws IOException {
        PDResources resources = page.getResources();
        Iterable<COSName> objectNames = resources.getXObjectNames();
        if (objectNames != null) {
            for (COSName objectName : objectNames) {
                if (resources.isImageXObject(objectName)) {
                    PDImageXObject image = (PDImageXObject) resources.getXObject(objectName);

                    // Сжатие изображения с заданным качеством
                    BufferedImage bufferedImage = image.getImage();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, image.getSuffix(), outputStream);
                    ByteArrayInputStream inputImageStream = new ByteArrayInputStream(outputStream.toByteArray());
                    BufferedImage compressedImage = ImageIO.read(inputImageStream);
                    ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(compressedImage, image.getSuffix(), compressedOutputStream);

                    // Создание нового объекта изображения из сжатого изображения с заданным качеством
                    PDImageXObject compressedPDImage = PDImageXObject.createFromByteArray(document, compressedOutputStream.toByteArray(), "compressed_image");

                    // Замена исходного изображения сжатым
                    resources.put(objectName, compressedPDImage);
                }
            }
        }
    }
}
