package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.cartas.Cartas;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
import java.io.*;
import java.nio.file.Path;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarCartaController {

    Cartas cartas;
    EscritorDeArquivos escritorDeArquivos;

    @Autowired
    public SalvarCartaController(Cartas cartas, EscritorDeArquivos escritorDeArquivos) {
        this.cartas = cartas;
        this.escritorDeArquivos = escritorDeArquivos;
    }

    @RequestMapping(value = "/editar/v3/servico/{id}", method = POST)
    RedirectView salvar(@PathVariable("id") Carta carta,
                        @RequestBody DOMSource servico,
                        @AuthenticationPrincipal User usuario) throws Exception {

        String doc = formata(servico);

        cartas.comRepositorioAberto(git -> {

            cartas.pull(git);

            try {
                return cartas.executaNoBranchDoServico(carta, () -> {
                    Path caminho = carta.getCaminhoAbsoluto();
                    Path dir = caminho.getParent();

                    if (dir.toFile().mkdirs()) {
                        log.debug("Diretório {} não existia e foi criado", dir);
                    } else {
                        log.debug("Diretório {} já existia e não precisou ser criado", dir);
                    }

                    String mensagem = format("%s '%s'", caminho.toFile().exists() ? "Altera" : "Cria", carta);

                    escritorDeArquivos.escrever(caminho, doc);
                    cartas.add(git, caminho);
                    cartas.commit(git, mensagem, usuario, caminho);

                    return null;
                });

            } finally {
                cartas.push(git, carta);
            }
        });

        return new RedirectView("/editar/api/servico/v3/" + carta.getId());
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
