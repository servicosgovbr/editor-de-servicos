package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.xml.ArquivoXml;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ImportadorServicoV1 {

    File repositorioCartasLocal;

    @Autowired
    public ImportadorServicoV1(File repositorioCartasLocal) {
        this.repositorioCartasLocal = repositorioCartasLocal;
    }

    public Servico carregar(String id) throws IOException {
        ArquivoXml xml = new ArquivoXml(
                format("%s/cartas-servico/v1/servicos/%s.xml", repositorioCartasLocal.getPath(), id),
                "servico"
        );

        return new Servico()
                .withNome(xml.texto("nome"))
                .withDescricao(xml.html("descricao") + "\n\n" + informacoesUteis(xml))
                .withOrgao(xml.converte("orgaoResponsavel", this::orgao))
                .withEventosDaLinhaDaVida(xml.coleta("eventosDaLinhaDaVida > eventoDaLinhaDaVida > nome"))
                .withAreasDeInteresse(xml.coleta("areasDeInteresse > areaDeInteresse > nome"))
                .withSegmentosDaSociedade(xml.coleta("segmentosDaSociedade > segmentoDaSociedade > nome",
                        ArquivoXml::texto,
                        x -> "Serviços aos Cidadãos".equals(x) ? "Cidadãos" : x,
                        x -> "Serviços às Empresas".equals(x) ? "Empresas" : x
                ))
                .withEtapas(singletonList(new Etapa()
                        .withTitulo("Acesso ao serviço")
                        .withCustos(singletonList(new Custo().withValor(xml.texto("custoTotalEstimado"))))
                        .withCanaisDePrestacao(
                                Stream.concat(
                                        Stream.of(
                                                new CanalDePrestacao().withTipo("Web").withDescricao(xml.texto("url")).withPreferencial(true),
                                                new CanalDePrestacao().withTipo("Agendamento").withDescricao(xml.texto("urlAgendamento"))
                                        ),
                                        xml.coleta("canaisDePrestacao > canalDePrestacao",
                                                x -> new CanalDePrestacao()
                                                        .withTipo(x.atributo("tipo"))
                                                        .withDescricao(x.atributo("link", "href")))
                                                .stream())
                                        .collect(toList()))));
    }

    private Orgao orgao(ArquivoXml doc) {
        return new Orgao()
                .withId(doc.texto("id"))
                .withNome(doc.texto("nome"));
    }

    private String informacoesUteis(ArquivoXml doc) {
        return doc.coleta("informacoesUteis > informacaoUtil", x -> "* [" + x.texto("descricao") + "](" + x.atributo("link", "href") + ")\n")
                .stream().collect(joining());
    }

}
