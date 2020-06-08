package com.proarea.api.swagger;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ID_TOKEN_HEADER = "ID-Token";

    private final SwaggerProperties properties;

    @Autowired
    public SwaggerConfig(SwaggerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.proarea.api"))
                .build()
                .securitySchemes(Arrays.asList(bearerToken(), idToken()))
                .securityContexts(securityContexts())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getApiTitle())
                .description(properties.getApiDescription())
                .build();
    }

    @Bean
    SecurityScheme bearerToken() {
        return new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, In.HEADER.name());
    }

    @Bean
    SecurityScheme idToken() {
        return new ApiKey(ID_TOKEN_HEADER, null, In.HEADER.name());
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> contexts = new ArrayList<>();
        properties.getSecuredPaths().forEach(path ->
                contexts.add(SecurityContext.builder().forPaths(PathSelectors.ant(path)).build()));
        return contexts;
    }

}
