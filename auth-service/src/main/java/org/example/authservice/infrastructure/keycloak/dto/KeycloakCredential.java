package org.example.authservice.infrastructure.keycloak.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakCredential {
    private String type;
    private String value;
    private boolean temporary;

    public static KeycloakCredential password(String value) {
        KeycloakCredential cred = new KeycloakCredential();
        cred.setType("password");
        cred.setValue(value);
        cred.setTemporary(false);
        return cred;
    }
}
