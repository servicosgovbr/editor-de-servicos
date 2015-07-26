'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function () {
    this.servico = new modelos.Servico();
  },

  view: function (ctrl) {
    return m('', [
      m.component(require('componentes/cabecalho'), {
        servico: ctrl.servico
      }),

      m.component(require('componentes/menu-lateral'), {
        servico: ctrl.servico
      }),

      m('#principal.auto-grid', [

        m.component(require('componentes/dados-basicos'), {
          servico: ctrl.servico
        }),

        m.component(require('componentes/solicitantes'), {
          solicitantes: ctrl.servico.solicitantes()
        }),

        m.component(require('componentes/etapas'), {
          etapas: ctrl.servico.etapas(),
          gratuidade: ctrl.servico.gratuidade
        }),

        m.component(require('componentes/dados-complementares'), {
          servico: ctrl.servico
        }),
      ])
    ]);
  }
};
