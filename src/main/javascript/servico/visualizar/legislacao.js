'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
    this.converter = new window.showdown.Converter();

    this.temLegislacao = function () {
      return !_.isEmpty(this.servico.legislacoes());
    };
  },

  view: function (ctrl) {
    if (ctrl.temLegislacao()) {
      return m('#servico-legislacao', [
              m('h3.subtitulo-servico', 'Legislação'),
              m('ul', ctrl.servico.legislacoes().map(function (legislacao) {
          return m('li', m('span', m.trust(ctrl.converter.makeHtml(legislacao))));
        }))
          ]);
    }
    return m.component(require('servico/visualizar/view-vazia'));
  }
};
