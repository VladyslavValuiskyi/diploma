package com.proarea.api.swagger;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("swagger")
@Data
@NoArgsConstructor
public class SwaggerProperties {

    private String groupName;
    private List<String> securedPaths;
    private String apiTitle;
    private String apiDescription;

}
