package br.gov.servicos.editor.servicos;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ServicoController {

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
        Document doc = Jsoup.parse(servico, "UTF-8");

        doc.outputSettings().prettyPrint(false); // respeita formatação de CDATA

        return new Servico()
                .withNome(doc.select("servico > nome").text().trim())
                .withDescricao(doc.select("servico > descricao").html().trim());
    }

}
