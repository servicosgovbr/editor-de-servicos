package br.gov.servicos.editor.utils;

import br.gov.servicos.editor.oauth2.google.security.UserProfile;

public class TestData {

    public static final UserProfile GOOGLE_PROFILE = new UserProfile()
            .withName("fulano")
            .withEmail("servicos@planejamento.gov.br");

}
