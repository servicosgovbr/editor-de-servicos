package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Servico;
import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.jsoup.nodes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.valueOf;
import static java.util.Collections.emptyList;
import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExportadorServicoV2 {

    Slugify slugify;

    @Autowired
    public ExportadorServicoV2(Slugify slugify) {
        this.slugify = slugify;
    }

    public Document exportar(Servico servico) {

        Document document = new Document("");
        document.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml)
                .prettyPrint(true);

        document.children().remove();

        Element root = document
                .appendChild(new StandardXmlDeclaration())
                .appendElement("servico")
                .attr("versao", "2")
                .appendElement("nome").text(servico.getNome()).parent()
                .appendElement("nomes-populares").text(servico.getNomesPopulares()).parent()
                .appendElement("descricao").appendChild(new DataNode(servico.getDescricao(), "")).parent()
                .appendElement("palavras-chave").text(servico.getPalavrasChave()).parent();

        areasDeInteresse(servico, root);
        segmentosDaSociedade(servico, root);
        eventosDaLinhaDaVida(servico, root);
        legislacaoRelacionada(servico, root);
        solicitantes(servico, root);
        tempoTotalEstimado(servico, root);
        etapas(servico, root);
        orgao(servico, root);
        gratuito(servico, root);
        situacaoDoServico(servico, root);

        return document;
    }

    private void situacaoDoServico(Servico servico, Element root) {
        Optional.ofNullable(servico.getSituacao()).ifPresent(s ->
                root.appendElement("situacao").text(s));
    }

    private void gratuito(Servico servico, Element root) {
        Optional.ofNullable(servico.getGratuito()).ifPresent(o ->
                root.appendElement("gratuito").text(o.toString()));
    }

    private void orgao(Servico servico, Element root) {
        Optional.ofNullable(servico.getOrgao()).ifPresent(o ->
                root.appendElement("orgao")
                        .appendElement("id").text(o.getId()).parent()
                        .appendElement("nome").text(o.getNome()));
    }

    private void etapas(Servico servico, Element root) {
        Element etapas = root.appendElement("etapas");
        Optional.ofNullable(servico.getEtapas()).orElse(emptyList())
                .stream()
                .forEach(e -> {
                            Element etapa = etapas.appendElement("etapa")
                                    .appendElement("titulo").text(e.getTitulo()).parent()
                                    .appendElement("descricao").appendChild(new DataNode(e.getDescricao(), "")).parent()
                                    .appendElement("documentos").parent();

                            Element custos = etapa.appendElement("custos").attr("excecoes", Optional.ofNullable(e.getCustoTemExcecoes()).orElse(false).toString());
                            e.getCustos().forEach(c ->
                                    custos.appendElement("custo")
                                            .appendElement("descricao").appendChild(new DataNode(c.getDescricao(), "")).parent()
                                            .appendElement("valor").text(c.getValor()));

                            Element canais = etapa.appendElement("canais-de-prestacao");
                            e.getCanaisDePrestacao().forEach(c ->
                                    canais.appendElement("canal-de-prestacao")
                                            .attr("tipo", c.getTipo())
                                            .attr("preferencial", valueOf(c.getPreferencial()))
                                            .appendElement("descricao").text(c.getDescricao()));
                        }
                );
    }

    private void tempoTotalEstimado(Servico servico, Element root) {
        Optional.ofNullable(servico.getTempoEstimado()).ifPresent(t -> {
                    Element tempo = root.appendElement("tempo-total-estimado");

                    Optional.ofNullable(t.getTipo()).ifPresent(tipo -> {
                        if ("entre".equals(tipo)) {
                            tempo.attr("tipo", tipo)
                                    .appendElement("minimo").attr("tipo", t.getEntreTipoMinimo()).text(t.getEntreMinimo()).parent()
                                    .appendElement("maximo").attr("tipo", t.getEntreTipoMaximo()).text(t.getEntreMaximo()).parent()
                                    .appendElement("excecoes").text(t.getExcecoes());
                        } else if ("até".equals(tipo)) {
                            tempo.attr("tipo", tipo)
                                    .appendElement("maximo").attr("tipo", t.getAteTipoMaximo()).text(t.getAteMaximo()).parent()
                                    .appendElement("excecoes").text(t.getExcecoes());
                        }
                    });
                }
        );
    }

    private void solicitantes(Servico servico, Element root) {
        Element solicitantes = root.appendElement("solicitantes");
        Optional.ofNullable(servico.getSolicitantes()).orElse(emptyList())
                .stream()
                .forEach(s -> solicitantes.appendElement("solicitante").text(s));
    }

    private void legislacaoRelacionada(Servico servico, Element root) {
        Element legislacao = root.appendElement("legislacao-relacionada");
        Optional.ofNullable(servico.getLegislacoes()).orElse(emptyList())
                .stream()
                .forEach(link -> legislacao.appendElement("link").attr("href", link));
    }

    private void eventosDaLinhaDaVida(Servico servico, Element root) {
        Element eventos = root.appendElement("eventos-da-linha-da-vida");
        Optional.ofNullable(servico.getEventosDaLinhaDaVida()).orElse(emptyList())
                .stream()
                .forEach(e -> eventos.appendElement("evento-da-linha-da-vida")
                        .appendElement("id").text(slugify.slugify(e)).parent()
                        .appendElement("nome").text(e).parent());
    }

    private void segmentosDaSociedade(Servico servico, Element root) {
        Element segmentos = root.appendElement("segmentos-da-sociedade");
        Optional.ofNullable(servico.getSegmentosDaSociedade()).orElse(emptyList())
                .stream()
                .forEach(s -> segmentos.appendElement("segmento-da-sociedade")
                        .appendElement("id").text(slugify.slugify(s)).parent()
                        .appendElement("nome").text(s).parent());
    }

    private void areasDeInteresse(Servico servico, Element root) {
        Element areas = root.appendElement("areas-de-interesse");
        Optional.ofNullable(servico.getAreasDeInteresse()).orElse(emptyList())
                .stream()
                .forEach(a -> areas.appendElement("area-de-interesse")
                        .appendElement("id").text(slugify.slugify(a)).parent()
                        .appendElement("area").text(a).parent());
    }
}
