package br.gov.servicos.editor.security;


import br.gov.servicos.editor.usuarios.Papel;
import br.gov.servicos.editor.usuarios.PapelRepository;
import br.gov.servicos.editor.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AcessoCidadaoService {

    public static final String CIDADAO = "CIDADAO";

    PapelRepository papelRepository;

    @Autowired
    public AcessoCidadaoService(PapelRepository papelRepository) {
       this.papelRepository = papelRepository;
    }

    public void autenticaCidadao(String nome, String email, String cpf) {
        Usuario usuario = criaUsuario(nome, email, cpf);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, CIDADAO,usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Usuario criaUsuario(String nome, String email, String cpf) {
        Papel papel = papelRepository.findByNome(CIDADAO);
        return new Usuario().withNome(nome).withEmailPrimario(email).withCpf(cpf).withPapel(papel);
    }
}
