package br.gov.servicos.editor.servicos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Date;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Metadados {
    String id;
    String revisao;
    String autor;
    Date horario;

    public Metadados(RevCommit commit) {
        this.revisao = commit.getId().getName();
        this.autor = commit.getAuthorIdent().getName();
        this.horario = commit.getAuthorIdent().getWhen();
    }
}
