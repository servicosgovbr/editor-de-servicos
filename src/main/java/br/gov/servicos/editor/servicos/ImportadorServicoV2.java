package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.xml.ArquivoXml;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ImportadorServicoV2 {

    Cartas cartas;

    @Autowired
    public ImportadorServicoV2(Cartas cartas) {
        this.cartas = cartas;
    }

    @SneakyThrows
    public Optional<Servico> carregar(String id) {
        return cartas.conteudoServicoV2(id)
                .map(this::parse)
                .map(xml -> xml.select("servico").first())
                .map(ArquivoXml::new)
                .map(this::carregar)
                .map(s -> s.withMetadados(cartas.ultimaRevisao(id)));
    }

    @SneakyThrows
    private Element parse(String conteudo) {
        return Jsoup.parse(conteudo, "/", Parser.xmlParser());
    }

    public Servico carregar(ArquivoXml xml) {
        return new Servico()
                .withNome(xml.texto("nome"))
                .withNomesPopulares(xml.texto("nomes-populares"))
                .withDescricao(xml.html("descricao"))
                .withPalavrasChave(xml.texto("palavras-chave"))
                .withSegmentosDaSociedade(xml.coleta("segmentos-da-sociedade > segmento-da-sociedade nome"))
                .withEventosDaLinhaDaVida(xml.coleta("eventos-da-linha-da-vida > evento-da-linha-da-vida nome"))
                .withLegislacoes(xml.coleta("legislacao-relacionada > link", l -> l.atributo("href")))
                .withSolicitantes(xml.coleta("solicitantes > solicitante"))
                .withGratuito(xml.textoAtivo("gratuito"))
                .withSituacao(xml.texto("situacao"))
                .withAreasDeInteresse(xml.coleta("areas-de-interesse > area-de-interesse area"))
                .withTempoEstimado(xml.converte("tempo-total-estimado", t -> {
                    if ("entre".equals(t.atributo("tipo"))) {
                        return new TempoEstimado()
                                .withTipo(t.atributo("tipo"))
                                .withEntreTipoMinimo(t.atributo("minimo", "tipo"))
                                .withEntreMinimo(t.texto("minimo"))
                                .withEntreTipoMaximo(t.atributo("maximo", "tipo"))
                                .withEntreMaximo(t.texto("maximo"))
                                .withExcecoes(t.texto("excecoes"));
                    } else if ("até".equals(t.atributo("tipo"))) {
                        return new TempoEstimado()
                                .withTipo(t.atributo("tipo"))
                                .withAteTipoMaximo(t.atributo("maximo", "tipo"))
                                .withAteMaximo(t.texto("maximo"))
                                .withExcecoes(t.texto("excecoes"));
                    }

                    return null;
                }))

                .withEtapas(xml.coleta("etapas > etapa", e ->
                        new Etapa()
                                .withTitulo(e.texto("titulo"))
                                .withDescricao(e.texto("descricao"))
                                .withDocumentos(e.coleta("documentos documento"))
                                .withCustos(e.coleta("custos custo",
                                        c -> new Custo()
                                                .withDescricao(c.texto("descricao"))
                                                .withValor(c.texto("valor"))))

                                .withCanaisDePrestacao(
                                        e.coleta("canais-de-prestacao canal-de-prestacao",
                                                c -> new CanalDePrestacao()
                                                        .withTipo(c.atributo("tipo"))
                                                        .withTitulo(c.texto("canal-de-prestacao > titulo"))
                                                        .withReferencia(c.texto("canal-de-prestacao > referencia"))
                                                        .withPreferencial(c.atrituboAtivo("preferencial"))))))

                .withOrgao(xml.converte("orgao",
                        o -> new Orgao()
                                .withId(o.texto("id"))
                                .withNome(o.texto("nome"))));
    }
}