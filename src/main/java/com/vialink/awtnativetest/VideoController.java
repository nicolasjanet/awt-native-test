package com.vialink.awtnativetest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Controller
@RequiredArgsConstructor
@Slf4j
public class VideoController {

    private final AwtNativeTestProperties properties;

    @PostMapping(value = "/video", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> generate(ServerHttpResponse response) throws IOException {
        var image = this.grabFrame(properties.getVideo().getFile());
        final HttpHeaders respHeaders = response.getHeaders();
        respHeaders.set(CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
        respHeaders.setContentDisposition(
                ContentDisposition.inline()
                        .filename("output/" + UUID.randomUUID() + ".jpg", StandardCharsets.UTF_8)
                        .build());
        return response.writeWith(Mono.just(image));
    }

    private DataBuffer grabFrame(File file) throws IOException {
        log.info("File exists ? {}", file.exists());
        FFmpegLogCallback.set();
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file)) {
            grabber.start();
            Frame frame = grabber.grabImage();
            try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
                BufferedImage bufferedImage = converter.convert(frame);
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    ImageIO.write(bufferedImage, "jpeg", baos);
                    byte[] bytes = baos.toByteArray();
                    grabber.stop();
                    return DefaultDataBufferFactory.sharedInstance.wrap(bytes);
                }
            }

        }
    }
}
