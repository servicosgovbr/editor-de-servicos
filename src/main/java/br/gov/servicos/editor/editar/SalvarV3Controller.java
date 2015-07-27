package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Cartas;
import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarV3Controller {

    Cartas cartas;
    Slugify slugify;

    @Autowired
    public SalvarV3Controller(Cartas cartas, Slugify slugify) {
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/v3/servico/{id}", method = POST)
    void salvar(@PathVariable("id") String unsafeId,
                  @RequestBody DOMSource servico,
                  @AuthenticationPrincipal User usuario) throws IOException, TransformerException {

        String id = slugify.slugify(unsafeId);
        cartas.salvarServicoV3(id, servico, usuario);
    }

}
