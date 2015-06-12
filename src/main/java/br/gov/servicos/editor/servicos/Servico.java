package br.gov.servicos.editor.servicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.List;

import static java.util.Collections.singletonList;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    String nome;
    String nomesPopulares;
    String descricao;
    String palavrasChave;

    List<String> solicitantes = singletonList("");

    Boolean gratuito;
    String situacao;

    List<String> areasDeInteresse = singletonList("");
    List<String> eventosDaLinhaDaVida = singletonList("");
    List<String> segmentosDaSociedade = singletonList("");

    List<String> legislacoes = singletonList("");
}
