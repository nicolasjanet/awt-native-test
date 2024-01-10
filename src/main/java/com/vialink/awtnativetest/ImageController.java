package com.vialink.awtnativetest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    private final PDFService pdfService;

    @PostMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> generate(ServerHttpResponse response) throws IOException {
        var document = pdfService.generate();
        var image = pdfService.toImage(document);
        DataBufferFactory dbf = new DefaultDataBufferFactory();
        final HttpHeaders respHeaders = response.getHeaders();
        respHeaders.set(CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
        respHeaders.setContentDisposition(
                ContentDisposition.inline()
                        .filename("output/" + UUID.randomUUID() + ".jpg", StandardCharsets.UTF_8)
                        .build());
        return response.writeWith(DataBufferUtils.read(new ByteArrayResource(image.toByteArray()), dbf, 128));
    }

}
