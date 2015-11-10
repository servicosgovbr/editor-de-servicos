package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.git.Revisao;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class MetadadosUtils {

    public static HashMap<String, String> metadados(ConteudoVersionado conteudoVersionado) {
        Metadados metadados = conteudoVersionado.getMetadados();

        HashMap<String, String> headers = Maps.newHashMap();
        ofNullable(metadados.getPublicado()).ifPresent(r -> headers.putAll(metadadosHeaders(r, "Publicado")));
        ofNullable(metadados.getEditado()).ifPresent(r -> headers.putAll(metadadosHeaders(r, "Editado")));

        return headers;
    }

    private static Map<String, String> metadadosHeaders(Revisao r, String sufixo) {
        return ImmutableMap.of(
                "X-Git-Commit-" + sufixo, r.getHash(),
                "X-Git-Autor-" + sufixo, r.getAutor(),
                "X-Git-Horario-" + sufixo, String.valueOf(r.getHorario().getTime()));
    }
}
