package ru.taras_buriachenko.NauJava.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class Config {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

}
