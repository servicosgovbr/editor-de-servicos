package br.gov.servicos.editor.oauth2.google.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Getter
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile {
    String id;

    String email;

    @JsonProperty("verified_email")
    Boolean verifiedEmail;

    String name;

    @JsonProperty("given_name")
    String givenName;

    @JsonProperty("family_name")
    String familyName;

    String link;

    String picture;

    String gender;

    String locale;

    String hd;
}