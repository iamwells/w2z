package io.github.iamwells.w2zserver.properties;


import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Data
public class TokenProperties {
    /**
     * Token的Header Name
     */
    private String name = "w2z-token";

    private Boolean blackListAutoCleanTaskIfCreate = true;

    private Integer blackListAutoCleanTimeInterval = 1;

    private TimeUnit blackListAutoCleanTimeUnit = TimeUnit.DAYS;

}
