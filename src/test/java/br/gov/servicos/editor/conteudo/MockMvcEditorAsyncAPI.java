package br.gov.servicos.editor.conteudo;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

@AllArgsConstructor
public class MockMvcEditorAsyncAPI {

    private MockMvc mvc;
    private MockMvcEditorAPI api;

    public ResultActions editarCarta(String id) throws Exception {
        return performAsync(api.editarCarta(id));
    }

    public ResultActions editarNovaCarta() throws Exception {
        return performAsync(api.editarNovaCarta());
    }

    public ResultActions renomearCarta(String de, String para) throws Exception {
        return performAsync(api.renomearCarta(de, para));
    }

    public ResultActions salvarCarta(String id, String conteudo) throws Exception {
        return performAsync(api.salvarCarta(id, conteudo));
    }

    public ResultActions publicarCarta(String id) throws Exception {
        return performAsync(api.publicarCarta(id));
    }

    public ResultActions descartarCarta(String id) throws Exception {
        return performAsync(api.descartarCarta(id));
    }

    public ResultActions listar() throws Exception {
        return performAsync(api.listar());
    }

    @SneakyThrows
    private ResultActions performAsync(ResultActions resultActions) {
        MvcResult result = resultActions
                .andExpect(MockMvcResultMatchers.request().asyncStarted())
                .andReturn();

        return mvc.perform(asyncDispatch(result));
    }

}
