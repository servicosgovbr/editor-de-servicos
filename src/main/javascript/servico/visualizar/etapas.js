'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;

    this.temEtapas = function () {
      return !_.isEmpty(this.servico.etapas());
    };
  },

  view: function (ctrl) {
    if (ctrl.temEtapas()) {
      return m('#servico-etapas', [
              m('h3.subtitulo-servico', 'Etapas para a realização deste serviço'),
              m.component(require('servico/visualizar/etapa'), ctrl.servico)
          ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));
  }
};
