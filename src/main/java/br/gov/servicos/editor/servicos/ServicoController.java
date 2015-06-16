package br.gov.servicos.editor.servicos;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ServicoController {

    private static final String UTF_8 = "UTF-8";

    File repositorioCartasLocal;

    @Autowired
    ServicoController(File repositorioCartasLocal) {
        this.repositorioCartasLocal = repositorioCartasLocal;
    }

    @RequestMapping(value = "/servico/{id}", method = GET)
    ModelAndView editar(@PathVariable("id") String id) {
        return new ModelAndView("index", "servico", carregaServico(id));
    }

    @SneakyThrows
    private Servico carregaServico(String id) {
        File servico = new File(format("%s/cartas-servico/v1/servicos/%s.xml", repositorioCartasLocal.getPath(), id));
        Document doc = Jsoup.parse(servico, UTF_8);

        doc.outputSettings().prettyPrint(false); // respeita formatação de CDATA

        Servico ser = new Servico()
                .withNome(doc.select("servico > nome").text().trim())
                .withNomesPopulares(doc.select("servico > nomes-populares").text().trim())
                .withDescricao(doc.select("servico > descricao").html().trim())
                .withPalavrasChave(doc.select("servico > palavras-chave").text().trim())
                .withAreasDeInteresse(
                        doc.select("servico > areas-de-interesse area-de-interesse").stream()
                                .map(area -> area.select("area-de-interesse > nome").text().trim()).collect(toList()))
                .withSegmentosDaSociedade(
                        doc.select("servico > segmentos-da-sociedade segmento-da-sociedade").stream()
                                .map(segmento -> segmento.select("segmento-da-sociedade > nome").text().trim()).collect(toList()))
                .withEventosDaLinhaDaVida(
                        doc.select("servico > eventos-da-linha-da-vida evento-da-linha-da-vida").stream()
                                .map(e -> e.select("evento-da-linha-da-vida > nome").text().trim()).collect(toList()))
                .withLegislacoes(
                        doc.select("servico > legislacao-relacionada link").stream()
                                .map(l -> l.attr("href").trim()).collect(toList()))
                .withSolicitantes(
                        doc.select("servico > solicitantes solicitante").stream()
                                .map(s -> s.text().trim()).collect(toList()))
                .withTempoEstimado(
                        doc.select("servico > tempo-total-estimado").stream()
                                .map(t -> {
                                            Elements minimo = t.select("tempo-total-estimado > minimo");
                                            Elements maximo = t.select("tempo-total-estimado > maximo");

                                            return new TempoEstimado()
                                                    .withTipo(t.attr("tipo"))
                                                    .withTipoMinimo(minimo.attr("tipo"))
                                                    .withMinimo(minimo.text().trim())
                                                    .withTipoMaximo(maximo.attr("tipo"))
                                                    .withMaximo(maximo.text().trim())
                                                    .withExcecoes(t.select("tempo-total-estimado > excecoes").text().trim());
                                        }
                                ).findFirst().orElse(null))
                .withEtapas(
                        doc.select("servico > etapas etapa").stream()
                                .map(e -> new Etapa()
                                                .withTitulo(e.select("etapa > titulo").text().trim())
                                                .withDescricao(e.select("etapa > descricao").text().trim())
                                                .withDocumentos(
                                                        e.select("etapa > documentos documento").stream()
                                                                .map(d -> d.text().trim())
                                                                .collect(toList())
                                                )
                                                .withCustos(
                                                        e.select("custos custo").stream()
                                                                .map(c -> new Custo()
                                                                                .withDescricao(c.select("custo > descricao").text().trim())
                                                                                .withValor(c.select("custo > valor").text().trim())
                                                                ).collect(toList())
                                                )
                                                .withCanaisDePrestacao(
                                                        e.select("canais-de-prestacao canal-de-prestacao").stream()
                                                                .map(c -> new CanalDePrestacao()
                                                                        .withTipo(c.attr("tipo").trim())
                                                                        .withDescricao(c.select("canal-de-prestacao > descricao").text().trim())
                                                                        .withPreferencial(c.attr("preferencial").trim().equals("true"))
                                                                ).collect(toList())
                                                )
                                )
                                .collect(toList())
                )
                .withOrgao(
                        doc.select("servico > orgao").stream()
                        .map(o -> new Orgao()
                                        .withId(o.select("orgao > id").text().trim())
                                        .withNome(o.select("orgao > nome").text().trim())
                        ).findFirst().orElse(null)
                )
                .withGratuito(doc.select("servico > gratuito").text().trim().equals("true"))
                .withSituacao(doc.select("servico > situacao").text().trim());

        System.out.println(ser);

        return ser;
    }

}
