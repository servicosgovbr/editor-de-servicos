'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;

    this.temSolicitante = function () {
      return !_.isEmpty(this.servico.solicitantes());
    };
  },

  view: function (ctrl) {
    if (ctrl.temSolicitante()) {
      return m('#servico-solicitantes', [
            m('h3.subtitulo-servico', 'Quem pode utilizar este serviço?'),
            m.component(require('servico/visualizar/solicitante'), ctrl.servico.solicitantes())
        ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));
  }
};
