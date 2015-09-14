'use strict';

var focus = require('focus');

module.exports = {
  view: function (ctrl, args) {
    ctrl.adicionado = args.adicionado;
    var titulo = args.titulo;
    var indice = args.indice;

    return m('.titulo', [
      m('h3', [
        'Título da etapa ' + (indice + 1),
        m.component(require('tooltips').tituloDaEtapa)
      ]),

      m('div.input-container', {
        class: titulo.erro()
      }, [
        m('input[type=text]', {
          onkeyup: m.withAttr('value', titulo),
          config: focus(ctrl),
          value: titulo()
        })
      ])
    ]);
  }
};
