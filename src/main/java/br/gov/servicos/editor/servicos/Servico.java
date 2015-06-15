package br.gov.servicos.editor.servicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.List;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    String nome;
    String nomesPopulares;
    String descricao;
    String palavrasChave;
    List<String> solicitantes;
    TempoEstimado tempoEstimado;
    Boolean gratuito;
    String situacao;
    List<Etapa> etapas;
    Orgao orgao;
    List<String> areasDeInteresse;
    List<String> eventosDaLinhaDaVida;
    List<String> segmentosDaSociedade;
    List<String> legislacoes;

}
