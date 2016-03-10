package br.gov.servicos.editor.security;


import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.usuarios.UsuarioRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component(value = "userDetailsService")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EditorUserDetailsService implements UserDetailsService {

    UsuarioRepository usuarios;

    CPFFormatter formatter;

    @Autowired
    public EditorUserDetailsService(UsuarioRepository usuarios) {
        formatter = new CPFFormatter();
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        return usuarios.findByCpf(formatter.unformat(cpf));
    }
}
