package br.gov.servicos.editor.usuarios.cadastro;


import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class CpfUnicoValidator implements ConstraintValidator<CpfUnico, Object> {
    @Autowired
    private UsuarioRepository repository;

    private CPFFormatter cpfFormatter = new CPFFormatter();
    protected String nomeCampoMarcaSeValidacaoAtiva;
    protected String nomeCampoComValorCpf;

    @Override
    public void initialize(CpfUnico constraintAnnotation) {
        nomeCampoMarcaSeValidacaoAtiva = constraintAnnotation.campoMarcaSeValidacaoHabilitada();
        nomeCampoComValorCpf = constraintAnnotation.nomeCampoComValorCpf();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Field campoValidacaoAtiva = ReflectionUtils.findField(value.getClass(), nomeCampoMarcaSeValidacaoAtiva);
        ReflectionUtils.makeAccessible(campoValidacaoAtiva);
        boolean validacaoEstaAtiva = (boolean) ReflectionUtils.getField(campoValidacaoAtiva, value);

        if (!validacaoEstaAtiva) {
            return true;
        }

        Field campoCpf = ReflectionUtils.findField(value.getClass(), nomeCampoComValorCpf);
        ReflectionUtils.makeAccessible(campoCpf);
        String cpf = (String) ReflectionUtils.getField(campoCpf, value);

        return repository.findByCpf(cpfFormatter.unformat(cpf)) == null;
    }
}
