'use strict';

var slugify = require('slugify');
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

var mySet = {};

function orgaoConteudo(s) {
    if (s.conteudo.orgao) {
        mySet[s.conteudo.orgao.id] = true;
        var cabecalho = new modelos.Cabecalho();
        var servico = carregarServico(slugify(s.id), cabecalho);
        servico.then(function(ser) {
            var novoOrgao = mapper[ser.orgao()];
            ser.orgao(novoOrgao);
            salvarServico(m.prop(ser), cabecalho.metadados);
            window.console.log('atualizou servico', ser.id,ser.orgao());
        });
    }
}

module.exports = {
  controller: function (args) {
      window.console.log('hehe');
    this.servicos = m.prop([]);

    this.servicosFiltrados = function () {
      var servicos = this.servicos();
      return _.sortBy(servicos, 'id');
    };

    this.listarConteudos = _.debounce(function () {
      m.request({
          method: 'GET',
          url: '/editar/api/conteudos'
        })
        .then(this.servicos, erro)
          .then(function (x) {
              _.forEach(x, function(s) {
                  orgaoConteudo(s);
              });
          }).then(function() {
              window.console.log('fim');
          });
    }.bind(this), 500);

    this.listarConteudos();
  },

  view: function (ctrl) {
    moment.locale('pt-br');
      window.console.log('haha');
    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('componentes/cabecalho')),

        m('#bem-vindo', [
          m('h2', 'Bem-vindo!'),

          m('table', [
            m('tr', [
              m('th[width="40%"]', 'Nome'),
              m('th.center', 'Publicação'),
              m('th.center', 'Orgão'),
              m('th.right', '')
            ])
          ].concat(ctrl.servicosFiltrados().map(function (s) {
            var iconesDeTipo = {
              servico: 'fa-file-text-o',
              orgao: 'fa-building-o'
            };

            return m('tr', [

              m('td', m('a', {
                href: '/editar/' + s.conteudo.tipo + '/' + slugify(s.id)
              }, [
                m('span.fa', {
                  class: iconesDeTipo[s.conteudo.tipo] || 'fa-file-o'
                }),
                m.trust(' &nbsp; '),
                s.conteudo.nome
              ])),

              m('td.center', s.publicado ? [
                moment(s.publicado.horario).fromNow(),
                ', por ',
                s.publicado.autor
              ] : '—'),
              m('td.center',
                s.conteudo.orgao ? s.conteudo.orgao.id : '-'
               ),

              m('td.right', [
                m('a', {
                  href: '/editar/servico/' + slugify(s.id)
                }, m('button', [
                  m('span.fa.fa-pencil')
                ]))
              ])
            ]);
          })))
        ])
      ])
    ]);
  }
};
