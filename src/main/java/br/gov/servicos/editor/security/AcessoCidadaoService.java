package br.gov.servicos.editor.security;


import br.gov.servicos.editor.usuarios.FormularioAcessoCidadao;
import br.gov.servicos.editor.usuarios.PapelRepository;
import br.gov.servicos.editor.usuarios.Usuario;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcessoCidadaoService {

    public static final String CIDADAO = "CIDADAO";

    PapelRepository papeis;

    @Autowired
    public AcessoCidadaoService(PapelRepository papeis) {
        this.papeis = papeis;
    }

    public void autenticaCidadao(FormularioAcessoCidadao cidadao) {
        Usuario usuario = new Usuario()
                .withNome(cidadao.getNome())
                .withEmailPrimario(cidadao.getEmail())
                .withCpf(cidadao.getCpf())
                .withPapel(papeis.findByNome(CIDADAO));

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                usuario, CIDADAO, usuario.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);
    }

}
