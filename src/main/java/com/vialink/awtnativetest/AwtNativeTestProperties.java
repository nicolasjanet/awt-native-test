package com.vialink.awtnativetest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties
@Getter
@Setter
public class AwtNativeTestProperties {

    private Resource video;
}
