'use strict';

var erro = require('utils/erro-ajax');
var carregarServico = require('xml/carregar');
var modelos = require('modelos');
var salvarServico = require('xml/salvar');

var mapper = {
  'ministerio-do-planejamento-orcamento-e-gestao-mp': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/2981',
  'ministerio-da-previdencia-social-mps': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1930',
  'secretaria-da-receita-federal-do-brasil-rfb': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/77',
  'ministerio-da-justica-mj': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/316',
  'ministerio-do-desenvolvimento-agrario-mda': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/17125',
  'ministerio-da-saude-ms': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/304',
  'ministerio-do-trabalho-e-emprego-mte': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/2844',
  'ministerio-da-agricultura-pecuaria-e-abastecimento-mapa': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/14',
  'ministerio-da-educacao-mec': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/244',
  'ministerio-do-desenvolvimento-social-e-combate-a-fome-mds': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1945',
  'ministerio-do-planejamento-orcamento-e-gestao-mpog': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/2981',
  'ministerio-do-turismo-mtur': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/72084',
  'banco-central-do-brasil-bcb': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/89',
  'ministerio-da-fazenda-mf': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1929',
  'secretaria-de-aviacao-civil-sac': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/115257',
  'ministerio-da-pesca-e-aquicultura-mpa': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/72083',
  'advocacia-geral-da-uniao-agu': 'http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/46'
};

module.exports = {
  controller: function (args) {
    this.servicos = m.prop([]);
    this.migrados = m.prop([]);

    this.listarConteudos = _.debounce(function () {
      m.request({
          method: 'GET',
          url: '/editar/api/conteudos'
        })
        .then(this.servicos, erro);
    }.bind(this), 500);

    this.migrar = function () {
      window.console.log('Iniciando migração...');
      var cabecalho = new modelos.Cabecalho();
      var migrados = this.migrados();
      var servicos = _.filter(this.servicos(), function (conteudo) {
        return !!conteudo.conteudo.orgao;
      });

      var defer = m.deferred();
      _.reduce(servicos, function (total, n) {
        return total.then(function () {
          return carregarServico(n.id, cabecalho).then(function (s) {
            s.orgao(mapper[s.orgao()]);

            return salvarServico(m.prop(s), cabecalho.metadados).then(function () {
              window.console.log('Servico ' + s.nome() + ' migrado com sucesso');
              migrados.push(s.nome());
            });
          });
        });
      }, defer.promise);

      defer.resolve();
    };

    this.listarConteudos();
  },

  view: function (ctrl) {
    moment.locale('pt-br');
    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('componentes/cabecalho')),

        m('#bem-vindo', [
          m('button', {
            onclick: _.bind(ctrl.migrar, ctrl)
          }, 'Migrar ', ctrl.servicos().length, ' serviços'),
          m('ul', _.map(ctrl.migrados(), function (s) {
            return m('li', s);
          }))
        ])
      ])
    ]);
  }
};
