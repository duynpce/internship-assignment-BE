package org.example.authservice.infrastructure.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KeycloakUserRepresentation {

    private String username;
    private String email;
    private Boolean enabled;
    private List<CredentialRepresentation> credentials;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CredentialRepresentation {
        private final String type = "password";
        private String value;
        private Boolean temporary;
    }
}