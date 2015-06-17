package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Servico;
import com.github.slugify.Slugify;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
class IndexController {

    @Autowired
    Slugify slugify;

    @RequestMapping("/")
    ModelAndView index() {
        return new ModelAndView("index", "servico", new Servico());
    }

    @RequestMapping(value = "/salvar", method = POST)
    RedirectView salvar(@ModelAttribute("servico") Servico servico, BindingResult result) {

        Document document = new Document("");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        document.children().remove();
        Element root = document.appendElement("servico")
                .appendElement("nome").text(servico.getNome()).parent()
                .appendElement("nomes-populares").text(servico.getNomesPopulares()).parent()
                .appendElement("descricao").text(servico.getDescricao()).parent()
                .appendElement("palavras-chave").text(servico.getPalavrasChave()).parent();

        Element areas = root.appendElement("areas-de-interesse");
        Optional.ofNullable(servico.getAreasDeInteresse()).orElse(emptyList())
                .stream()
                .forEach(a -> areas.appendElement("area-de-interesse")
                        .appendElement("id").text(slugify.slugify(a)).parent()
                        .appendElement("nome").text(a).parent());

        Element segmentos = root.appendElement("segmentos-da-sociedade");
        Optional.ofNullable(servico.getSegmentosDaSociedade()).orElse(emptyList())
                .stream()
                .forEach(s -> segmentos.appendElement("segmento-da-sociedade")
                        .appendElement("id").text(slugify.slugify(s)).parent()
                        .appendElement("nome").text(s).parent());

        Element eventos = root.appendElement("eventos-da-linha-da-vida");
        Optional.ofNullable(servico.getEventosDaLinhaDaVida()).orElse(emptyList())
                .stream()
                .forEach(e -> eventos.appendElement("evento-da-linha-da-vida")
                        .appendElement("id").text(slugify.slugify(e)).parent()
                        .appendElement("nome").text(e).parent());

        Element legislacao = root.appendElement("legislacao-relacionada");
        Optional.ofNullable(servico.getLegislacoes()).orElse(emptyList())
                .stream()
                .forEach(link -> legislacao.appendElement("link").attr("href", link));

        Element solicitantes = root.appendElement("solicitantes");
        Optional.ofNullable(servico.getSolicitantes()).orElse(emptyList())
                .stream()
                .forEach(s -> solicitantes.appendElement("solicitante").text(s));

        Optional.ofNullable(servico.getTempoEstimado()).ifPresent(t -> {
                    Element tempo = root.appendElement("tempo-total-estimado");

                    Optional.ofNullable(t.getTipo()).ifPresent(tipo -> {
                        if (tipo.equals("entre")) {
                            tempo.attr("tipo", tipo)
                                    .appendElement("minimo").attr("tipo", t.getEntreTipoMinimo()).text(t.getEntreMinimo()).parent()
                                    .appendElement("maximo").attr("tipo", t.getEntreTipoMaximo()).text(t.getEntreMaximo()).parent()
                                    .appendElement("excecoes").text(t.getExcecoes());
                        } else if (tipo.equals("até")) {
                            tempo.attr("tipo", tipo)
                                    .appendElement("maximo").attr("tipo", t.getAteTipoMaximo()).text(t.getAteMaximo()).parent()
                                    .appendElement("excecoes").text(t.getExcecoes());
                        }
                    });
                }
        );

        Element etapas = root.appendElement("etapas");
        Optional.ofNullable(servico.getEtapas()).orElse(emptyList())
                .stream()
                .forEach(e -> etapas.appendElement("etapa")
                                .appendElement("titulo").text(e.getTitulo()).parent()
                                .appendElement("descricao").text(e.getDescricao()).parent()
                                .appendElement("documentos").parent()
                                .appendElement("custos").attr("excecoes", Optional.ofNullable(e.getCustoTemExcecoes()).orElse(false).toString()).parent()
                                .appendElement("canais-de-prestacao").parent()
                );

        Optional.ofNullable(servico.getOrgao()).ifPresent(o ->
                root.appendElement("orgao")
                        .appendElement("id").text(o.getId()).parent()
                        .appendElement("nome").text(o.getNome()));

        Optional.ofNullable(servico.getGratuito()).ifPresent(o ->
                root.appendElement("gratuito").text(o.toString()));

        Optional.ofNullable(servico.getSituacao()).ifPresent(s ->
                root.appendElement("situacao").text(s));

        System.out.println("document = " + document);

        return new RedirectView("/");
    }

}
