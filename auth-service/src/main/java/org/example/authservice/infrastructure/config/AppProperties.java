package org.example.authservice.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
@Validated
public class AppProperties {

    @NotBlank(message = "client uri cannot be blank")
    private  String clientUri;

    @NotBlank(message = "user service url cannot be blank")
    private String userServiceUrl;
}
