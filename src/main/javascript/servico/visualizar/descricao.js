'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
  },

  view: function (ctrl) {
    return m('#servico-descricao', [
        m('h3.subtitulo-servico', 'O que é?'),
        m('.markdown', ctrl.servico.descricao())
    ]);
  }
};
