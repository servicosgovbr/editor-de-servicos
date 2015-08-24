'use strict';

var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.titulo = args.titulo;
    this.indice = args.indice;
    this.adicionado = args.adicionado;
  },

  view: function (ctrl) {
    return m('.titulo', [
      m('h3', [
        'Título da etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').tituloDaEtapa)
      ]),

      m('div.input-container', [
        m('input[type=text]', {
          onkeyup: m.withAttr('value', ctrl.titulo),
          config: focus(ctrl),
          value: ctrl.titulo()
        })
      ])
    ]);
  }
};
