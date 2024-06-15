package com.azkivam.banking.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class ExecutorConfig {

    private final Environment environment;

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(Integer.parseInt(Objects.requireNonNull(environment.getProperty("app.max.thread.count"))));
    }

}
