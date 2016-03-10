package br.gov.servicos.editor.usuarios.recuperarsenha;

import br.gov.servicos.editor.usuarios.Usuario;
import br.gov.servicos.editor.usuarios.token.CpfTokenInvalido;
import br.gov.servicos.editor.usuarios.token.TokenInvalido;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static net.logstash.logback.marker.Markers.append;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
public class RecuperarSenhaController {

    public static final String RECUPERAR_SENHA = "recuperarSenha";

    @Autowired
    private RecuperacaoSenhaService tokenService;

    @RequestMapping(value = "/editar/recuperar-senha", method = GET)
    public ModelAndView recuperacaoSenha(@RequestParam(value = "pagina", defaultValue = RECUPERAR_SENHA) String pagina,
                                         FormularioRecuperarSenha formularioRecuperarSenha) {
        ModelMap model = new ModelMap();
        model.addAttribute("pagina", pagina);
        model.addAttribute("formularioRecuperarSenha", formularioRecuperarSenha);
        return new ModelAndView("recuperar-senha", model);
    }

    @RequestMapping(value = "/editar/recuperar-senha", method = POST)
    public ModelAndView recuperarSenha(@RequestParam(value = "pagina", defaultValue = "recuperarSenha") String pagina,
                                       @Valid FormularioRecuperarSenha formularioRecuperarSenha, BindingResult result) {
        if (result.hasErrors()) {
            return retornarParaRecuperarSenha(pagina);
        }

        try {
            Usuario usuarioComSenhaNova = tokenService.trocarSenha(formularioRecuperarSenha);
            usuarioLog("Senha trocada", usuarioComSenhaNova);
            return new ModelAndView("redirect:/editar/autenticar?senhaAlterada");

        } catch (TokenInvalido e) {
            log.info("Falha na tentativa de trocar senha");
            result.addError(criarErroTokenInvalido(e));
            return retornarParaRecuperarSenha(pagina);
        }
    }

    private static FieldError criarErroTokenInvalido(TokenInvalido e) {
        String message;
        if (e instanceof CpfTokenInvalido) {
            CpfTokenInvalido cpfTokenInvalido = (CpfTokenInvalido) e;
            int tentativasSobrando = cpfTokenInvalido.getTentativasSobrando();
            message = criarMensagemTentativasSobrando(tentativasSobrando);
        } else {
            message = "Este link não é válido. Solicite um novo link para alterar sua senha.";
        }

        return new FieldError(
                FormularioRecuperarSenha.NOME_CAMPO,
                CamposVerificacaoRecuperarSenha.NOME,
                message);
    }

    private static String criarMensagemTentativasSobrando(int tentativasSobrando) {
        if (tentativasSobrando > 0) {
            return "O CPF informado não é compatível com o cadastrado. Você possui mais "
                    + tentativasSobrando + " tentativas.";
        } else {
            return "O CPF informado não é compatível com o cadastrado e este link foi bloqueado. " +
                    "Entre em contato com o responsável pelo seu órgão para solicitar um novo link..";
        }
    }

    private static ModelAndView retornarParaRecuperarSenha(
            @RequestParam(value = "pagina", defaultValue = RECUPERAR_SENHA) String pagina) {
        ModelMap model = new ModelMap();
        model.addAttribute("pagina", pagina);
        return new ModelAndView("recuperar-senha", model);
    }

    private static void usuarioLog(String mensagem, Usuario usuario) {
        Marker marker = append("usuario.id", usuario.getId())
                .and(append("usuario.cpf", usuario.getCpf()))
                .and(append("usuario.habilitado", usuario.isHabilitado()))
                .and(append("usuario.nome", usuario.getNome()))
                .and(append("usuario.papel", usuario.getPapel().getNome()))
                .and(append("usuario.siorg", usuario.getSiorg()));

        log.debug(marker, mensagem);
    }
}
