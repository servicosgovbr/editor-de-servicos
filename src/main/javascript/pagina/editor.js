'use strict';

var safeGet = require('utils/code-checks').safeGet;
var CabecalhoModel = require('cabecalho/cabecalho-model');
var carregarPagina = require('pagina/carregar');
var salvarOrgao = require('pagina/salvar');

module.exports = {

  controller: function () {
    this.cabecalho = new CabecalhoModel();
    this.pagina = carregarPagina(m.route.param('id'), this.cabecalho);

    this.modificado = m.prop(false);

    this.salvar = _.bind(function () {
      return salvarOrgao(this.pagina());
    }, this);
  },

  view: function (ctrl, args) {
    var tipo = safeGet(args, 'tipo');
    var componenteNome = safeGet(args, 'componenteNome');
    var tamanhoConteudo = safeGet(args, 'tamanhoConteudo');
    var tooltips = {
      tipo: safeGet(args, 'tooltips.tipo'),
      nome: safeGet(args, 'tooltips.nome'),
      conteudo: safeGet(args, 'tooltips.conteudo')
    };

    ctrl.pagina().tipo(tipo);

    var binding = {
      pagina: ctrl.pagina,
      novo: m.route.param('id') === 'novo'
    };

    return m('#conteudo', {
      config: function (element, isInitialized) {
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
      }
    }, [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('../cabecalho/cabecalho'), {
          metadados: true,
          logout: true,
          salvar: _.bind(ctrl.salvar, ctrl),
          cabecalho: ctrl.cabecalho
        }),
        m('#servico',
          m('.scroll', [
            m.component(require('pagina/componentes/tipo-de-pagina'), _.assign(binding, {
              tooltipTipo: tooltips.tipo
            })),
            m.component(require('pagina/componentes/nome'), _.assign(binding, {
              componente: componenteNome,
              tooltipNome: tooltips.nome
            })),
            m.component(require('pagina/componentes/conteudo'), _.assign(binding, {
              maximo: tamanhoConteudo,
              tooltipConteudo: tooltips.conteudo
            }))
          ])
        )
      ])
    ]);
  }
};
