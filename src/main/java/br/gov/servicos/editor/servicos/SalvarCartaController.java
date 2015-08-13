package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.cartas.Cartas;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
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

import javax.xml.transform.dom.DOMSource;
import java.nio.file.Path;

import static br.gov.servicos.editor.utils.Unchecked.Supplier.unchecked;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarCartaController {

    Cartas cartas;
    EscritorDeArquivos escritorDeArquivos;
    ReformatadorXml reformatadorXml;

    @Autowired
    public SalvarCartaController(Cartas cartas, EscritorDeArquivos escritorDeArquivos, ReformatadorXml reformatadorXml) {
        this.cartas = cartas;
        this.escritorDeArquivos = escritorDeArquivos;
        this.reformatadorXml = reformatadorXml;
    }

    @RequestMapping(value = "/editar/v3/servico/{id}", method = POST)
    RedirectView salvar(@PathVariable("id") Carta carta,
                        @RequestBody DOMSource servico,
                        @AuthenticationPrincipal User usuario) throws Exception {

        cartas.comRepositorioAberto(git -> {

            cartas.pull(git);

            try {
                return cartas.executaNoBranchDoServico(carta, unchecked(() -> {
                    Path caminho = carta.getCaminhoAbsoluto();

                    String mensagem = format("%s '%s'", caminho.toFile().exists() ? "Altera" : "Cria", carta);

                    escritorDeArquivos.escrever(caminho, reformatadorXml.formata(servico));

                    cartas.add(git, caminho);
                    cartas.commit(git, mensagem, usuario, caminho);

                    return null;
                }));

            } finally {
                cartas.push(git, carta);
            }
        });

        return new RedirectView("/editar/api/servico/v3/" + carta.getId());
    }

}
