package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.git.Revisao;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class MetadadosUtils {

    public static HttpHeaders metadados(ConteudoVersionado conteudoVersionado) {
        Metadados metadados = conteudoVersionado.getMetadados();

        HttpHeaders headers = new HttpHeaders();
        ofNullable(metadados.getPublicado()).ifPresent(r -> metadadosHeaders(headers, r, "Publicado"));
        ofNullable(metadados.getEditado()).ifPresent(r -> metadadosHeaders(headers, r, "Editado"));

        return headers;
    }

    private static void metadadosHeaders(HttpHeaders headers, Revisao r, String sufixo) {
        headers.add("X-Git-Commit-" + sufixo, r.getHash());
        headers.add("X-Git-Autor-" + sufixo, r.getAutor());
        headers.add("X-Git-Horario-" + sufixo, String.valueOf(r.getHorario().getTime()));
    }
}
