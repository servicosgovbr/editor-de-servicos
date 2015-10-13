package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.git.Metadados;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ListarConteudoController {

    ListaDeConteudo listaDeConteudo;

    @Autowired
    ListarConteudoController(ListaDeConteudo listaDeConteudo) {
        this.listaDeConteudo = listaDeConteudo;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/conteudos", method = GET)
    Iterable<Metadados> listar() throws IOException, ParseException {
        return listaDeConteudo.listar();
    }

}
