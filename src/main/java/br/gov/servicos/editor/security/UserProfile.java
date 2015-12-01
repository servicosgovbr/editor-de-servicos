package br.gov.servicos.editor.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Getter
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
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