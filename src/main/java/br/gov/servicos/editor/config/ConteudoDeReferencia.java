package br.gov.servicos.editor.config;

import br.gov.servicos.editor.servicos.AreaDeInteresse;
import br.gov.servicos.editor.servicos.Orgao;
import com.github.slugify.Slugify;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@Configuration
public class ConteudoDeReferencia {

    @Bean
    Collection<String> tiposDeCanalDePrestacao() {
        return asList(
                "Web",
                "Presencial",
                "Telefone",
                "E-mail",
                "Caixa postal",
                "Aplicativo móvel",
                "SMS",
                "Fax",
                "Rede social"
        );
    }

    @Bean
    Collection<Orgao> orgaos() {
        return asList(
                new Orgao("arquivo-nacional-an", "Arquivo Nacional (AN)"),
                new Orgao("banco-central-do-brasil-bcb", "Banco Central do Brasil (BCB)"),
                new Orgao("comissao-de-anistia-cma", "Comissão de Anistia (CMA)"),
                new Orgao("companhia-nacional-de-abastecimento-conab", "Companhia Nacional de Abastecimento (Conab)"),
                new Orgao("conselho-administrativo-de-defesa-economica-cade", "Conselho Administrativo de Defesa Econômica (Cade)"),
                new Orgao("defensoria-publica-da-uniao-dpu", "Defensoria Pública da União (DPU)"),
                new Orgao("departamento-de-policia-federal-dpf", "Departamento de Polícia Federal (DPF)"),
                new Orgao("departamento-de-policia-rodoviaria-federal-dprf", "Departamento de Polícia Rodoviária Federal (DPRF)"),
                new Orgao("empresa-brasileira-de-infraestrutura-aeroportuaria-infraero", "Empresa Brasileira de Infraestrutura Aeroportuária (Infraero)"),
                new Orgao("fundacao-nacional-do-indio-funai", "Fundação Nacional do Índio (Funai)"),
                new Orgao("instituto-nacional-de-meteorologia-inmet", "Instituto Nacional de Meteorologia (INMET)"),
                new Orgao("instituto-nacional-do-seguro-social-inss", "Instituto Nacional do Seguro Social (INSS)"),
                new Orgao("ministerio-da-agricultura-pecuaria-e-abastecimento-mapa", "Ministério da Agricultura, Pecuária e Abastecimento (MAPA)"),
                new Orgao("ministerio-da-educacao-mec", "Ministério da Educação (MEC)"),
                new Orgao("ministerio-da-fazenda-mf", "Ministério da Fazenda (MF)"),
                new Orgao("ministerio-da-justica-mj", "Ministério da Justiça (MJ)"),
                new Orgao("ministerio-da-pesca-e-aquicultura-mpa", "Ministério da Pesca e Aquicultura (MPA)"),
                new Orgao("ministerio-da-previdencia-social-mps", "Ministério da Previdência Social (MPS)"),
                new Orgao("ministerio-da-saude-ms", "Ministério da Saúde"),
                new Orgao("ministerio-do-desenvolvimento-agrario-mda", "Ministério do Desenvolvimento Agrário (MDA)"),
                new Orgao("ministerio-do-desenvolvimento-social-e-combate-a-fome-mds", "Ministério do Desenvolvimento Social e Combate a Fome (MDS)"),
                new Orgao("ministerio-do-planejamento-orcamento-e-gestao-mpog", "Ministério do Planejamento, Orçamento e Gestão (MPOG)"),
                new Orgao("ministerio-do-trabalho-e-emprego-mte", "Ministério do Trabalho e Emprego (MTE)"),
                new Orgao("ministerio-do-turismo-mtur", "Ministério do Turismo (MTur)"),
                new Orgao("secretaria-da-receita-federal-do-brasil-rfb", "Secretaria da Receita Federal do Brasil (RFB)"),
                new Orgao("secretaria-de-aviacao-civil-sac", "Secretaria de Aviação Civil (SAC)"),
                new Orgao("secretaria-de-planejamento-e-investimentos-estrategicos-spi", "Secretaria de Planejamento e Investimentos Estratégicos (SPI)"),
                new Orgao("secretaria-do-patrimonio-da-uniao-spu-mp", "Secretaria do Patrimônio da União (SPU)"),
                new Orgao("secretaria-nacional-de-justica-snj", "Secretaria Nacional de Justiça (SNJ)"),
                new Orgao("secretaria-nacional-de-politicas-sobre-drogas-senad", "Secretaria Nacional de Políticas Sobre Drogas (Senad)"),
                new Orgao("secretaria-nacional-de-seguranca-publica-senasp", "Secretaria Nacional de Segurança Pública (Senasp)")
        );
    }

    @Bean
    Collection<String> medidasDeTempo() {
        return asList(
                "minutos",
                "horas",
                "dias",
                "dias corridos",
                "dias úteis",
                "meses"
        );
    }

    @Bean
    Collection<String> segmentosDaSociedade() {
        return asList(
                "Cidadãos",
                "Empresas",
                "Governos",
                "Demais segmentos (ONGs, organizações sociais, etc)"
        );
    }

    @Bean
    Collection<String> eventosDaLinhaDaVida() {
        return asList(
                "Apoio financeiro e crédito",
                "Aposentadoria",
                "Contas e Impostos",
                "Cuidados com a saúde",
                "Documentos e certidões",
                "Empreendedorismo e negócios",
                "Estrangeiros no Brasil",
                "Estudos",
                "Exportação de produtos e serviços",
                "Família",
                "Importação de produtos e serviços",
                "Imóveis",
                "Profissão e trabalho",
                "Veículos",
                "Viagem ao exterior"
        );
    }

    @Bean
    Collection<AreaDeInteresse> areasDeInteresse() {
        return areasInteresse(
                "Administração",
                "Agropecuária",
                "Comércio e Serviços",
                "Comunicações",
                "Cultura",
                "Defesa Nacional",
                "Economia e Finanças",
                "Educação",
                "Energia",
                "Esporte e Lazer",
                "Habitação",
                "Indústria",
                "Meio-ambiente",
                "Pesquisa e Desenvolvimento",
                "Previdência Social",
                "Proteção Social",
                "Relações Internacionais",
                "Saneamento",
                "Saúde",
                "Segurança e Ordem Pública",
                "Trabalho",
                "Transportes",
                "Urbanismo");
    }


    @SneakyThrows
    private Collection<AreaDeInteresse> areasInteresse(String... areas) {
        Slugify slug = new Slugify();
        return Arrays.asList(areas).stream()
                .map(area -> new AreaDeInteresse()
                        .withId(slug.slugify(area))
                        .withArea(area))
                .collect(toList());
    }


}
