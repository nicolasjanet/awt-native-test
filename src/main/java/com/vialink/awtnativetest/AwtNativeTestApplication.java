package com.vialink.awtnativetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(AwtNativeTestRuntimeHintRegistrar.class)
@EnableConfigurationProperties(AwtNativeTestProperties.class)
public class AwtNativeTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwtNativeTestApplication.class, args);
	}

}
