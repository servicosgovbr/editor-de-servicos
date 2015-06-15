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
public class Etapa {

    String titulo;
    String descricao;
    List<Custo> custos;
    Boolean custoTemExcecoes;
    List<String> documentos;
    List<CanalDePrestacao> canaisDePrestacao;

}
