package br.gov.servicos.editor.editar;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
@Wither
@AllArgsConstructor
public class IndiceCampoDeEtapa {

    int indiceEtapa;
    int indiceCampo;

    public IndiceCampoDeEtapa() {
        indiceEtapa = -1;
        indiceCampo = -1;
    }

    private static Pattern INDICE_CAMPO = Pattern.compile("(?<etapa>\\d+),(?<campo>\\d+)");

    public static IndiceCampoDeEtapa from(String indice) {
        Objects.nonNull(indice);

        Matcher m = INDICE_CAMPO.matcher(indice);
        if (!m.find()) {
            throw new RuntimeException("Formato de indice de campo errado, esperado: 'num,num'. Encontrado: " + indice);
        }

        return new IndiceCampoDeEtapa()
                .withIndiceEtapa(Integer.parseInt(m.group("etapa")))
                .withIndiceCampo(Integer.parseInt(m.group("campo")));
    }
}