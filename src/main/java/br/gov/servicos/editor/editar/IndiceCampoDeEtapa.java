package br.gov.servicos.editor.editar;

import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Optional.*;
import static java.util.regex.Pattern.compile;

@Value
@Wither
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IndiceCampoDeEtapa {

    int etapa;
    int campo;

    private static Pattern INDICE_CAMPO = compile("(?<etapa>\\d+),(?<campo>\\d+)");

    private IndiceCampoDeEtapa(int etapa, int campo) {
        this.etapa = etapa;
        this.campo = campo;
    }

    public static IndiceCampoDeEtapa from(String indice) {
        return ofNullable(indice)
                .flatMap(i -> {
                    Matcher m = INDICE_CAMPO.matcher(i);
                    if (m.find()) {
                        return of(new IndiceCampoDeEtapa(
                                parseInt(m.group("etapa")),
                                parseInt(m.group("campo"))));
                    } else {
                        return empty();
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException(format("Índice de campo '%s' incorreto. Esperado: 'num,num'", indice)));
    }
}