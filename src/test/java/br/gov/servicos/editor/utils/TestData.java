package br.gov.servicos.editor.utils;

import br.gov.servicos.editor.oauth2.UserProfile;

public class TestData {

    public static final UserProfile PROFILE = new UserProfile()
            .withName("fulano")
            .withEmail("servicos@planejamento.gov.br");

}
