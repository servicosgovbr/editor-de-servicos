'use strict';

var Tooltips = require('tooltips');
var CabecalhoModel = require('cabecalho/cabecalho-model');
var EditorBase = require('componentes/editor-base');

var carregarPagina = require('xml/carregar').carregarOrgao;
var salvarOrgao = require('xml/salvar').salvarOrgao;
var redirecionarNovaPagina = require('redirecionador');

function ehNovo() {
  return m.route.param('id') === 'novo';
}

module.exports = {
  controller: function (args) {
    this.tipo = m.prop('orgao');

    this.modificado = m.prop(false);
    this.cabecalho = new CabecalhoModel();
    this.pagina = carregarPagina(m.route.param('id'), this.cabecalho);

    this.salvar = _.bind(function () {
      return salvarOrgao(this.pagina(), this.cabecalho.metadados)
        .then(this.pagina)
        .then(_.bind(function (pagina) {
          redirecionarNovaPagina(this.tipo(), pagina.url());
        }, this));
    }, this);
  },

  view: function (ctrl, args) {
    if (!ctrl.pagina()) {
      return m('');
    }

    var tamanhoConteudo = 1500;
    var tooltips = {
      tipo: Tooltips.tipoPagina,
      nome: Tooltips.escolhaOrgao,
      conteudo: Tooltips.conteudoOrgao
    };

    var binding = {
      pagina: ctrl.pagina,
      nome: ehNovo() ? ctrl.pagina().url : ctrl.pagina().nome,
      novo: ehNovo()
    };

    return m.component(EditorBase, {
      conteudoConfig: function (element, isInitialized) {
        if (isInitialized) {
          return;
        }
        jQuery(element).on('change', function () {
          ctrl.modificado(true);
        });
        jQuery(window).bind('beforeunload', function () {
          if (ctrl.modificado()) {
            return 'Suas últimas alterações ainda não foram salvas.';
          }
        });
      },

      cabecalhoConfig: {
        metadados: true,
        logout: true,
        salvar: _.bind(ctrl.salvar, ctrl),
        cabecalho: ctrl.cabecalho
      },

      componentes: [
        m.component(require('pagina/componentes/tipo-de-pagina'), {
          tipo: ctrl.tipo,
          tooltipTipo: tooltips.tipo
        }),

        m.component(require('pagina/componentes/nome'), _.assign(binding, {
          titulo: 'Selecione o órgão',
          componente: require('pagina/orgao/select-orgao'),
          tooltipNome: tooltips.nome,
        })),

        m.component(require('pagina/componentes/conteudo'), _.assign(binding, {
          maximo: tamanhoConteudo,
          tooltipConteudo: tooltips.conteudo
        }))
      ]
    });
  }
};
