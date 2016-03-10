package br.gov.servicos.editor.usuarios.token;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class GeradorToken {
    private final SecureRandom secureRandom = new SecureRandom();

    public String gerar() {
        return new BigInteger(130, secureRandom).toString(32);
    }
}
