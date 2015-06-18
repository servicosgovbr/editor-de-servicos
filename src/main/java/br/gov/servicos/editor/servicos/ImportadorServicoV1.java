package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.xml.ArquivoXml;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Collections.emptyList;
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

    @SneakyThrows
    public Optional<Servico> carregar(String id) {
        File arquivo = new File(format("%s/cartas-servico/v1/servicos/%s.xml", repositorioCartasLocal.getPath(), id));
        if (!arquivo.exists()) {
            return Optional.empty();
        }

        return carregar(new ArquivoXml(Jsoup.parse(arquivo, defaultCharset().name()).select("servico").first()));
    }

    public Optional<Servico> carregar(ArquivoXml xml) {
        return Optional.of(new Servico()
                .withNome(xml.texto("nome"))
                .withNomesPopulares(xml.texto("nomesPopulares"))
                .withPalavrasChave(xml.texto("palavrasChave"))
                .withSolicitantes(emptyList())
                .withSituacao("")
                .withLegislacoes(emptyList())
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
                        .withTitulo("")
                        .withDescricao("")
                        .withDocumentos(emptyList())
                        .withCustos(singletonList(new Custo().withDescricao("").withValor(xml.texto("custoTotalEstimado"))))
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
                                        .collect(toList())))));
    }

    private Orgao orgao(ArquivoXml doc) {
        return new Orgao()
                .withId(doc.texto("id"))
                .withNome(doc.texto("nome"));
    }

    private String informacoesUteis(ArquivoXml doc) {
        return doc.coleta("informacoesUteis > informacaoUtil", x -> format("* [%s](%s)\n", x.texto("descricao"), x.atributo("link", "href")))
                .stream()
                .collect(joining())
                .trim();
    }

}
