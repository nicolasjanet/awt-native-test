package com.vialink.awtnativetest;

import org.bytedeco.javacpp.presets.javacpp;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.plugins.jpeg.JPEGQTable;

public class AwtNativeTestRuntimeHintRegistrar implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.resources().registerPattern("org/apache/pdfbox/resources/*");
        hints.resources().registerPattern("big-buck-bunny_trailer.webm");

        /* JavaCV */
        hints.reflection().registerType(javacpp.class, MemberCategory.values());

        // Java2D
        hints.jni().registerType(TypeReference.of("sun.java2d.pipe.ShapeSpanIterator"), MemberCategory.values());

        // JPEG
        hints.jni().registerType(TypeReference.of("sun.awt.image.ByteComponentRaster"), MemberCategory.values());
        hints.jni().registerType(TypeReference.of("com.sun.imageio.plugins.jpeg.JPEGImageWriter"), MemberCategory.values());
        hints.jni().registerType(JPEGQTable.class, MemberCategory.values());
        hints.jni().registerType(JPEGHuffmanTable.class, MemberCategory.values());
    }

}
