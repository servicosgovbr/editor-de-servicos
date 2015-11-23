package br.gov.servicos.editor.conteudo;

import lombok.AllArgsConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.ALL;
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
        return mvc.perform(
                post("/editar/api/pagina/servico/" + id)
                        .content(conteudo)
                        .accept(ALL)
                        .contentType(APPLICATION_XML));
    }

    public ResultActions publicarCarta(String id) throws Exception {
        return mvc.perform(put("/editar/api/pagina/servico/" + id)
                .accept(ALL));
    }

    public ResultActions descartarCarta(String id) throws Exception {
        String url = String.format("/editar/api/pagina/servico/%s/descartar", id);
        return mvc.perform(post(url)
                .accept(ALL));
    }

    public ResultActions despublicarCarta(String id) throws Exception {
        String url = String.format("/editar/api/pagina/servico/%s/despublicar", id);
        return mvc.perform(post(url)
                .accept(ALL));
    }

    public ResultActions listar() throws Exception {
        return mvc.perform(get("/editar/api/conteudos")
                .accept(ALL));
    }

    public ResultActions editarPagina(String id, TipoPagina tipo) throws Exception {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + "/" + id)
                .accept(ALL));
    }

    public ResultActions editarPaginaNova(TipoPagina tipo) throws Exception {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + "/novo")
                .accept(ALL));
    }
}
