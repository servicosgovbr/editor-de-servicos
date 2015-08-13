package br.gov.servicos.editor.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;

@Slf4j
@Component
public class EscritorDeArquivos {

    @SneakyThrows
    public void escrever(Path caminho, String conteudo) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(caminho.toFile()), "UTF-8")) {
            writer.write(conteudo);
        }
        log.debug("Arquivo '{}' modificado", caminho.getFileName());
    }

}
