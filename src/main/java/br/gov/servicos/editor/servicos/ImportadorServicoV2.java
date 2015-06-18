package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.xml.ArquivoXml;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ImportadorServicoV2 {

    File repositorioCartasLocal;

    @Autowired
    public ImportadorServicoV2(File repositorioCartasLocal) {
        this.repositorioCartasLocal = repositorioCartasLocal;
    }

    @SneakyThrows
    public Optional<Servico> carregar(String id) {
        File arquivo = new File(format("%s/cartas-servico/v2/servicos/%s.xml", repositorioCartasLocal.getPath(), id));
        if(!arquivo.exists()) {
            return Optional.empty();
        }

        ArquivoXml xml = new ArquivoXml(Jsoup.parse(arquivo, defaultCharset().name()).select("servico").first());

        return Optional.of(new Servico()
                .withNome(xml.texto("nome"))
                .withNomesPopulares(xml.texto("nomes-populares"))
                .withDescricao(xml.texto("descricao"))
                .withPalavrasChave(xml.texto("palavras-chave"))
                .withAreasDeInteresse(xml.coleta("areas-de-interesse > area-de-interesse nome"))
                .withSegmentosDaSociedade(xml.coleta("segmentos-da-sociedade > segmento-da-sociedade nome"))
                .withEventosDaLinhaDaVida(xml.coleta("eventos-da-linha-da-vida > evento-da-linha-da-vida nome"))
                .withLegislacoes(xml.coleta("legislacao-relacionada > link", l -> l.atributo("href")))
                .withSolicitantes(xml.coleta("solicitantes > solicitante"))
                .withGratuito(xml.textoAtivo("gratuito"))
                .withSituacao(xml.texto("situacao"))

                .withTempoEstimado(xml.converte("tempo-total-estimado", t -> {
                    if (t.atributo("tipo").equals("entre")) {
                        return new TempoEstimado()
                                .withTipo(t.atributo("tipo"))
                                .withEntreTipoMinimo(t.atributo("minimo", "tipo"))
                                .withEntreMinimo(t.texto("minimo"))
                                .withEntreTipoMaximo(t.atributo("maximo", "tipo"))
                                .withEntreMaximo(t.texto("maximo"))
                                .withExcecoes(t.texto("excecoes"));
                    } else if (t.atributo("tipo").equals("até")) {
                        return new TempoEstimado()
                                .withTipo(t.atributo("tipo"))
                                .withAteTipoMaximo(t.atributo("maximo", "tipo"))
                                .withAteMaximo(t.texto("maximo"))
                                .withExcecoes(t.texto("excecoes"));
                    }

                    return null;
                }))

                .withEtapas(xml.coleta("servico > etapas > etapa", e ->
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
                                                        .withDescricao(c.texto("canal-de-prestacao > descricao"))
                                                        .withPreferencial(c.atrituboAtivo("preferencial"))))))

                .withOrgao(xml.converte("orgao",
                        o -> new Orgao()
                                .withId(o.texto("id"))
                                .withNome(o.texto("nome")))));
    }
}