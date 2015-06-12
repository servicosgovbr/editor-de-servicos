package br.gov.servicos.editor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Arrays.asList;

@Configuration
public class ConteudoDeReferencia {

    @Bean
    public List<String> segmentosDaSociedade() {
        return asList(
                "Cidadãos",
                "Empresas",
                "Governos",
                "Demais segmentos (ONGs, organizações sociais, etc)"
        );
    }

    @Bean
    public List<String> eventosDaLinhaDaVida() {
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
    public List<String> areasDeInteresse() {
        return asList(
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
                "Urbanismo"
        );
    }
}
