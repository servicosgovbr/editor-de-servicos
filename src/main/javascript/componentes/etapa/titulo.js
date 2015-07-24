'use strict';

module.exports = {

  controller: function (args) {
    this.titulo = args.titulo;
  },

  view: function (ctrl) {
    return m('.titulo', [
      m('h3', 'Título da etapa'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.titulo),
        value: ctrl.titulo()
      })
    ]);
  }
};
