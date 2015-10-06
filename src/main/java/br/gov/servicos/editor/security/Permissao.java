package br.gov.servicos.editor.security;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

public enum Permissao {
    EDITOR,
    ADMIN(EDITOR);

    @Getter
    final Set<Permissao> permissoes;

    Permissao(Permissao... others) {
        HashSet todas = new HashSet<>(asList(others));
        todas.add(this);
        this.permissoes = unmodifiableSet(todas);
    }

}
