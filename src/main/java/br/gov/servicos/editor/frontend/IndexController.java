package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Servico;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
class IndexController {

    ExportadorServicoV2 exportadorV2;

    @Autowired
    public IndexController(ExportadorServicoV2 exportadorV2) {
        this.exportadorV2 = exportadorV2;
    }

    @RequestMapping("/")
    ModelAndView index() {
        return new ModelAndView("index", "servico", new Servico());
    }

    @RequestMapping(value = "/salvar", method = POST)
    RedirectView salvar(@ModelAttribute("servico") Servico servico) {
        Document document = exportadorV2.exportar(servico);

        System.out.println("document = " + document);

        return new RedirectView("/");
    }

}
