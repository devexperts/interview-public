package com.devexperts.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    private final static String CONTROLLER_LAYER_PACKAGE = "com.devexperts.rest.controller";
    private final static String API_VERSION = "1.0";
    private final static String API_INFO_TITLE = "API which exposes a simple transfer operation";
    private final static String API_INFO_DESCRIPTION = "Simple amount transfer operation";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_LAYER_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(API_INFO_TITLE)
            .version(API_VERSION)
            .description(API_INFO_DESCRIPTION)
            .build();
    }
}
