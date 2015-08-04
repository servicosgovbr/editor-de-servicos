'use strict';

var slugify = require('slugify');

var toSelect2Object = function (c) {
  return {
    id: slugify(c),
    text: c
  };
};

module.exports = {
  areasDeInteresse: [
    'Abastecimento',
    'Administração financeira',
    'Agricultura orgânica',
    'Agropecuária',
    'Águas',
    'Alimento',
    'Ambiente e saúde',
    'Comunicações',
    'Comércio e Serviços',
    'Economia e Finanças',
    'Educação',
    'Educação básica',
    'Educação superior',
    'Educação à distância',
    'Emergências e Urgências',
    'Encargos financeiros',
    'Esporte e Lazer',
    'Finanças',
    'Habitação',
    'Humanização na saúde',
    'Indústria',
    'Meio ambiente',
    'Pecuária',
    'Pessoa',
    'Previdência Social',
    'Previdência social',
    'Profissionais da educação',
    'Proteção social',
    'Qualidade ambiental',
    'Relações Internacionais',
    'Saúde',
    'Saúde da criança',
    'Saúde da família',
    'Saúde da mulher',
    'Saúde do homem',
    'Saúde do idoso',
    'Saúde dos portadores de deficiências',
    'Segurança e Ordem Pública',
    'Trabalho',
    'Transportes',
    'Turismo',
    'Urbanismo',
  ],

  segmentosDaSociedade: [
    'Cidadãos',
    'Empresas',
    'Órgãos e entidades públicas',
    'Demais segmentos (ONGs, organizações sociais, etc)'
  ],

  eventosDaLinhaDaVida: [
    'Apoio financeiro e crédito',
    'Aposentadoria',
    'Contas e Impostos',
    'Cuidados com a saúde',
    'Documentos e certidões',
    'Empreendedorismo e negócios',
    'Estrangeiros no Brasil',
    'Estudos',
    'Exportação de produtos e serviços',
    'Família',
    'Importação de produtos e serviços',
    'Imóveis',
    'Profissão e trabalho',
    'Veículos',
    'Viagem ao exterior'
  ],

  tiposDeCanalDePrestacao: [
    'Web',
    'Web: Agendar',
    'Web: Inscrever-se',
    'Web: Simular',
    'Web: Calcular taxas',
    'Web: Acompanhar',
    'Web: Consultar',
    'Web: Emitir',
    'Web: Preencher',
    'Web: Declarar',
    'Presencial',
    'Telefone',
    'E-mail',
    'Postal',
    'Aplicativo móvel',
    'SMS',
    'Fax'
  ].map(toSelect2Object),

  tiposDeLegislacao: [
    'Acórdão de Relação',
    'Acórdão',
    'Ato Declaratório Interpretativo',
    'Ato Regimental',
    'Constituição',
    'Decisão',
    'Decreto Legislativo',
    'Decreto',
    'Decreto-Lei',
    'Emenda Constitucional',
    'Enunciado',
    'Instrução Normativa Conjunta',
    'Instrução Normativa',
    'Lei Complementar',
    'Lei Delegada',
    'Lei',
    'Medida Provisória',
    'Ordem de Serviço',
    'Orientação Jurisprudencial',
    'Orientação Normativa',
    'Portaria Conjunta',
    'Portaria Interministerial',
    'Portaria Normativa',
    'Portaria',
    'Provimento',
    'Recomendação',
    'Regimento Interno',
    'Resolucão Conjunta',
    'Resolução Autorizativa',
    'Resolução Homologatória',
    'Resolução Normativa',
    'Resolução',
    'Súmula',
  ].map(toSelect2Object),

  orgaos: _.once(m.request({
    method: 'GET',
    url: '/editar/api/orgaos'
  }).then(function (orgaos) {
    return _.sortBy(orgaos.unidades.map(function (o) {
      var nome = o.nome + ' (' + o.sigla + ')';
      return {
        id: slugify(nome),
        text: nome
      };
    }), function (o) {
      return o.text;
    });
  }))

};
