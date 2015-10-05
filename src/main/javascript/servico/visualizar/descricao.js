'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.converter = new window.showdown.Converter();
  },

  view: function (ctrl) {
    return m('#servico-descricao', [
        m('h3.subtitulo-servico', 'O que é?'),
        m('.markdown', m.trust(ctrl.converter.makeHtml(ctrl.servico.descricao())))
    ]);
  }
};
