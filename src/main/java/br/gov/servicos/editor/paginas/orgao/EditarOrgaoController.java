package br.gov.servicos.editor.paginas.orgao;


import br.gov.servicos.editor.cartas.PaginaDeOrgao;
import br.gov.servicos.editor.servicos.Metadados;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.Arrays;

import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditarOrgaoController {
    @ResponseBody
    @RequestMapping(value = "/editar/api/orgao/{id}", method = GET, produces = "application/json")
    public String editar(
            @PathVariable("id") PaginaDeOrgao pagina,
            HttpServletResponse response
    ) throws FileNotFoundException {
        Metadados<PaginaDeOrgao.Orgao> metadados = pagina.getMetadados();

        ofNullable(metadados.getPublicado()).ifPresent(r -> {
            response.setHeader("X-Git-Commit-Publicado", r.getHash());
            response.setHeader("X-Git-Autor-Publicado", r.getAutor());
            response.setDateHeader("X-Git-Horario-Publicado", r.getHorario().getTime());
        });

        ofNullable(metadados.getEditado()).ifPresent(r -> {
            response.setHeader("X-Git-Commit-Editado", r.getHash());
            response.setHeader("X-Git-Autor-Editado", r.getAutor());
            response.setDateHeader("X-Git-Horario-Editado", r.getHorario().getTime());
        });

        PaginaDeOrgao.Orgao orgao = carregarConteudoPagina(pagina);

        return converterParaJson(orgao);
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/orgao/novo", method = GET, produces = "application/json")
    PaginaDeOrgao.Orgao editarNovo() {
        return new PaginaDeOrgao.Orgao();
    }

    @SneakyThrows
    private String converterParaJson(PaginaDeOrgao.Orgao pagina) throws FileNotFoundException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(pagina);
    }

    private PaginaDeOrgao.Orgao carregarConteudoPagina(PaginaDeOrgao pagina) throws FileNotFoundException {
        String[] linhas = pagina.getConteudoRaw().split("\n");
        int posicaoCabecalhoConteudo = linhas.length > 2 ? 3 : linhas.length;

        PaginaDeOrgao.Orgao orgao = pagina.getMetadados().getConteudo();
        orgao.setConteudo(String.join("\n", Arrays.copyOfRange(linhas, posicaoCabecalhoConteudo, linhas.length)));

        return orgao;
    }
}
