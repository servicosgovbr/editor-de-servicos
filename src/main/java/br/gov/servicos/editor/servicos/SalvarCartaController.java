package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarCartaController {

    Cartas cartas;
    Slugify slugify;

    @Autowired
    public SalvarCartaController(Cartas cartas, Slugify slugify) {
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @RequestMapping(value = "/editar/v3/servico/{id}", method = POST)
    RedirectView salvar(@PathVariable("id") String unsafeId,
                  @RequestBody DOMSource servico,
                  @AuthenticationPrincipal User usuario) throws IOException, TransformerException {

        String id = slugify.slugify(unsafeId);
        String doc = formata(servico);

        cartas.salvarServico(id, doc, usuario);

        return new RedirectView("/editar/api/servico/v3/" + id);
    }

    private String formata(@RequestBody DOMSource servico) throws TransformerException {
        StringWriter writer = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StreamResult result = new StreamResult(writer);
        transformer.transform(servico, result);

        return writer.toString();
    }

}
