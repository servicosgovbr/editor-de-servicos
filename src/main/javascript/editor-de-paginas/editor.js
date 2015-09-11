'use strict';

var modelos = require('modelos');
var carregarPagina = require('editor-de-paginas/carregar');

var modificado = m.prop(false);

module.exports = {

  controller: function () {
    this.cabecalho = new modelos.Cabecalho();
    this.pagina = carregarPagina(m.route.param('id'), this.cabecalho);

    this.salvar = function () {};
  },

  view: function (ctrl) {
    var binding = {
      pagina: ctrl.pagina
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
            m.component(require('editor-de-paginas/componentes/tipo-de-pagina'), binding),
            m.component(require('editor-de-paginas/componentes/nome'), binding),
            m.component(require('editor-de-paginas/componentes/conteudo'), binding)
          ])
        )
      ])
    ]);
  }
};
