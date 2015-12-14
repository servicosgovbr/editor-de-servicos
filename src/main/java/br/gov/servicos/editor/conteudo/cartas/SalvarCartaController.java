package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoMetadadosProvider;
import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.security.CheckOrgaoEspecificoController;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import br.gov.servicos.editor.utils.DeserializadorUtils;
import br.gov.servicos.editor.utils.ReformatadorXml;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.transform.dom.DOMSource;

import java.lang.reflect.UndeclaredThrowableException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarCartaController extends CheckOrgaoEspecificoController {

    ReformatadorXml reformatadorXml;
    UserProfiles userProfiles;
    ConteudoVersionadoFactory factory;
    Siorg siorg;


    @Autowired
    public SalvarCartaController(ReformatadorXml reformatadorXml, UserProfiles userProfiles, ConteudoVersionadoFactory factory, Siorg siorg) {
        this.reformatadorXml = reformatadorXml;
        this.userProfiles = userProfiles;
        this.factory = factory;
        this.siorg = siorg;
    }

    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = POST)
    RedirectView salvar(
            @PathVariable("tipo") String tipo,
            @PathVariable("id") String id,
            @RequestBody DOMSource servico) throws Exception {
        TipoPagina tipoPagina = TipoPagina.fromNome(tipo);
        String conteudo = reformatadorXml.formata(servico);
        ConteudoVersionado conteudoVersionado = factory.pagina(id, tipoPagina);

        String orgaoId = "";
        try {
            orgaoId = conteudoVersionado.getOrgaoId();
        } catch (UndeclaredThrowableException ex) { // ele pode não encontrar o arquivo com o xml - acontece na criação da página
            ConteudoMetadadosProvider conteudoMetadadosProvider = DeserializadorUtils.obterDeserializador(tipoPagina).deserializaConteudo(conteudo);
            orgaoId = conteudoMetadadosProvider.toConteudoMetadados(siorg).getOrgaoId();
        }


        if (!usuarioPodeRealizarAcao(userProfiles, tipoPagina, orgaoId)) {
            throw new AccessDeniedException("Usuário sem permissão");
        }


        conteudoVersionado.salvar(userProfiles.get(), conteudo);
        return new RedirectView("/editar/api/pagina/" + tipo + "/" + conteudoVersionado.getId(), true, false);
    }

    @Override
    public TipoPermissao getTipoPermissao() {
        return TipoPermissao.EDITAR_SALVAR;
    }
}