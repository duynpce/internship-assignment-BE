package org.example.authservice.infrastructure.keycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserRepresentation {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private List<KeycloakCredential> credentials;

    public static KeycloakUserRepresentation of(String username, String email,
                                                 String firstName, String lastName, String password) {
        KeycloakUserRepresentation user = new KeycloakUserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setCredentials(List.of(KeycloakCredential.password(password)));
        return user;
    }
}
