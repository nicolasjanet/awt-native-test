package com.vialink.awtnativetest;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;

@Service
public class PDFService {

    public PDDocument generate() throws IOException {
        var document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        addContent(document, page);
        return document;
    }

    public ByteArrayOutputStream toByteArrayOutputStream(PDDocument document) throws IOException {
        var baos = new ByteArrayOutputStream();
        document.save(baos);
        return baos;
    }

    public ByteArrayOutputStream toImage(PDDocument document) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImage(0, 1);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "JPG", out);
        return out;
    }

    private void addContent(PDDocument document, PDPage page) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.COURIER, 12);
        contentStream.beginText();
        contentStream.showText("Hello World " + Instant.now());
        contentStream.endText();
        contentStream.close();
    }
}
