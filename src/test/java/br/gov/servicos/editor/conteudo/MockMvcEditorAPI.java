package br.gov.servicos.editor.conteudo;

import lombok.AllArgsConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AllArgsConstructor
public class MockMvcEditorAPI {

    private MockMvc mvc;

    public ResultActions editarCarta(String id) throws Exception {
        return mvc.perform(get("/editar/api/pagina/servico/" + id)
                .accept(ALL));
    }

    public ResultActions editarNovaCarta() throws Exception {
        return editarCarta("novo");
    }

    public ResultActions renomearCarta(String de, String para) throws Exception {
        return mvc.perform(patch(String.format("/editar/api/pagina/servico/%s/%s", de, para))
                .accept(ALL));
    }

    public ResultActions salvarCarta(String id, String conteudo) throws Exception {
        return salvarPagina(SERVICO, id, conteudo);
    }

    public ResultActions salvarPagina(TipoPagina tipo, String id, String conteudo) throws Exception {
        return mvc.perform(
                post("/editar/api/pagina/" + tipo.getNome() + "/" + id)
                        .content(conteudo)
                        .accept(ALL)
                        .contentType(APPLICATION_XML));
    }

    public ResultActions publicarPagina(TipoPagina tipo, String id) throws Exception {
        return mvc.perform(put("/editar/api/pagina/" + tipo.getNome() + "/" + id)
                .accept(ALL));
    }

    public ResultActions publicarCarta(String id) throws Exception {
        return publicarPagina(SERVICO, id);
    }

    public ResultActions descartarPagina(TipoPagina tipo, String id) throws Exception {
        String url = String.format("/editar/api/pagina/%s/%s/descartar", tipo.getNome(), id);
        return mvc.perform(post(url)
                .accept(ALL));
    }

    public ResultActions descartarCarta(String id) throws Exception {
        return descartarPagina(SERVICO, id);
    }

    public ResultActions despublicarCarta(String id) throws Exception {
        return descartarPagina(SERVICO, id);
    }

    public ResultActions despublicarPagina(TipoPagina tipo, String id) throws Exception {
        String url = String.format("/editar/api/pagina/" + tipo.getNome() + "/%s/despublicar", id);
        return mvc.perform(post(url)
                .accept(ALL));
    }

    public ResultActions listar() throws Exception {
        return mvc.perform(get("/editar/api/conteudos")
                .accept(ALL));
    }

    public ResultActions editarPagina(TipoPagina tipo, String id) throws Exception {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + "/" + id)
                .accept(ALL));
    }

    public ResultActions editarPaginaNova(TipoPagina tipo) throws Exception {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + "/novo")
                .accept(ALL));
    }

}
