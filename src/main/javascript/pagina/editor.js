'use strict';

var CabecalhoModel = require('cabecalho/cabecalho-model');
var carregarPagina = require('pagina/carregar');
var salvarOrgao = require('pagina/salvar');

var modificado = m.prop(false);

module.exports = {

  controller: function () {
    this.cabecalho = new CabecalhoModel();
    this.pagina = carregarPagina(m.route.param('id'), this.cabecalho);

    this.salvar = _.bind(function () {
      return salvarOrgao(this.pagina());
    }, this);
  },

  view: function (ctrl) {
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
          modificado(true);
        });

        jQuery(window).bind('beforeunload', function () {
          if (modificado()) {
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
            m.component(require('pagina/componentes/tipo-de-pagina'), binding),
            m.component(require('pagina/componentes/nome'), binding),
            m.component(require('pagina/componentes/conteudo'), _.assign(binding, {
              maximo: 1500
            }))
          ])
        )
      ])
    ]);
  }
};
