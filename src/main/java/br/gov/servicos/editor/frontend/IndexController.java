package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Servico;
import br.gov.servicos.editor.servicos.TempoEstimado;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Controller
public class IndexController {

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index", "servico", new Servico());
    }

    @RequestMapping("/exemplo")
    public ModelAndView exemplo() {
        return new ModelAndView("index", "servico", new Servico()
                .withNome("Carteira Nacional de Habilitação (CNH)")
                .withNomesPopulares("carta de motorista, carteira, carta, cnh, habilitação")
                .withDescricao("A CNH blah blah blah...")
                .withPalavrasChave("carta de motorista, carteira, carta, cnh, habilitação")
                .withAreasDeInteresse(new ArrayList<>(asList("Comércio e Serviços", "Comunicações")))
                .withSegmentosDaSociedade(new ArrayList<>(asList("Cidadãos", "Empresas")))
                .withEventosDaLinhaDaVida(new ArrayList<>(asList("Documentos e certidões", "Contas e Impostos")))
                .withLegislacoes(new ArrayList<>(asList(
                        "http://www.lexml.gov.br/urn/urn:lex:br:federal:decreto:2009-08-11;6932",
                        "http://www.lexml.gov.br/urn/urn:lex:br:federal:lei:1993-06-21;8666")))
                .withSolicitantes(new ArrayList<>(singletonList("Cidadãos maiores de 18 anos")))
                .withTempoEstimado(new TempoEstimado()
                                .withTipo("entre")
                                .withMinimo("15")
                                .withTipoMinimo("dias")
                                .withMaximo("20")
                                .withTipoMaximo("dias")
                                .withExcecoes("Para solicitantes dos tipos C, D e E, o processo pode levar mais tempo.")
                )
                .withGratuito(true)
                .withSituacao("Sim: serviço parcialmente eletrônico (partes presenciais, via telefone ou em papel)")
        );
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    RedirectView salvar(@ModelAttribute("servico") Servico servico, BindingResult result) {
        return new RedirectView("/");
    }

}
