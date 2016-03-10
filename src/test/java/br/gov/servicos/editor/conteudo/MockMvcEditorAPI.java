package br.gov.servicos.editor.conteudo;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static java.lang.String.format;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AllArgsConstructor
public class MockMvcEditorAPI {

    private MockMvc mvc;

    @SneakyThrows
    public ResultActions editarCarta(String id) {
        return mvc.perform(get("/editar/api/pagina/servico/" + id)
                .accept(ALL));
    }

    public ResultActions editarNovaCarta() {
        return editarCarta("novo");
    }

    @SneakyThrows
    public ResultActions renomearCarta(String id, String novoNome) {
        return mvc.perform(patch(format("/editar/api/pagina/servico/%s", id))
                .contentType(APPLICATION_JSON)
                .content(novoNome)
                .accept(ALL));
    }

    @SneakyThrows
    public ResultActions salvarCarta(String id, String conteudo) {
        return salvarPagina(SERVICO, id, conteudo);
    }

    @SneakyThrows
    public ResultActions salvarPagina(TipoPagina tipo, String id, String conteudo) {
        return mvc.perform(
                post("/editar/api/pagina/" + tipo.getNome() + '/' + id)
                        .content(conteudo)
                        .accept(ALL)
                        .contentType(APPLICATION_XML));
    }

    @SneakyThrows
    public ResultActions publicarPagina(TipoPagina tipo, String id) {
        return mvc.perform(put("/editar/api/pagina/" + tipo.getNome() + '/' + id)
                .accept(ALL));
    }

    @SneakyThrows
    public ResultActions publicarCarta(String id) {
        return publicarPagina(SERVICO, id);
    }

    @SneakyThrows
    public ResultActions descartarPagina(TipoPagina tipo, String id) {
        String url = format("/editar/api/pagina/%s/%s/descartar", tipo.getNome(), id);
        return mvc.perform(post(url)
                .accept(ALL));
    }

    @SneakyThrows
    public ResultActions descartarCarta(String id) {
        return descartarPagina(SERVICO, id);
    }

    @SneakyThrows
    public ResultActions despublicarCarta(String id) {
        return despublicarPagina(SERVICO, id);
    }

    @SneakyThrows
    public ResultActions despublicarPagina(TipoPagina tipo, String id) {
        String url = format("/editar/api/pagina/" + tipo.getNome() + "/%s/despublicar", id);
        return mvc.perform(post(url)
                .accept(ALL));
    }

    @SneakyThrows
    public ResultActions listar() {
        return mvc.perform(get("/editar/api/conteudos")
                .accept(ALL));
    }

    @SneakyThrows
    public ResultActions excluirPagina(TipoPagina tipo, String id) {
        return mvc.perform(delete("/editar/api/pagina/" + tipo.getNome() + '/' + id)
                .accept(ALL));
    }

    @SneakyThrows
    public ResultActions editarPagina(TipoPagina tipo, String id) {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + '/' + id)
                .accept(ALL));
    }

    @SneakyThrows
    public ResultActions editarPaginaNova(TipoPagina tipo) {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + "/novo")
                .accept(ALL));
    }
}
