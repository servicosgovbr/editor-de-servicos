package br.gov.servicos.editor.fixtures.given;


import java.io.File;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.util.Arrays;
        import java.util.List;

public class EstruturaRepositorio {

    public static Path orgaos() {
        return Paths.get("conteudo", "orgaos");
    }

    public static Path paginasEspeciais() {
        return Paths.get("conteudo", "paginas-especiais");
    }

    public static Path areasDeInteresse() {
        return Paths.get("conteudo", "areas-de-interesse");
    }

    public static Path cartas() {
        return Paths.get("cartas-servico", "v3", "servicos");
    }

    public static List<Path> estrutura() {
        return Arrays.asList(orgaos(), paginasEspeciais(), areasDeInteresse(), cartas());
    }

    public static void mkdirs(Path raiz) {
        estrutura().stream()
                .map(raiz::resolve)
                .map(Path::toFile)
                .forEach(File::mkdirs);
    }

}