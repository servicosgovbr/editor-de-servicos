package br.gov.servicos.editor.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.joining;

@Slf4j
@Component
public class LeitorDeArquivos {

    @SneakyThrows
    public Optional<String> ler(File arquivo) {
        if (!arquivo.exists()) {
            log.info("Arquivo {} não encontrado", arquivo.getAbsolutePath());
            return empty();

        } else if (!arquivo.isFile() || !arquivo.canRead()) {
            log.info("Arquivo {} não pode ser lido", arquivo.getAbsolutePath());
            return empty();
        }

        log.info("Arquivo {} encontrado", arquivo.getAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), defaultCharset()))) {
            return of(reader.lines().collect(joining("\n")));
        }
    }
}
