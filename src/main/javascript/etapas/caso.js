'use strict';

module.exports = function (componente) {

  return {

    controller: function (args) {
      this.caso = args.caso;
      this.padrao = !!args.padrao;
    },

    view: function (ctrl) {
      var titulo;
      if (ctrl.padrao) {
        titulo = m('label.titulo', 'Para todos os casos');
      } else {
        titulo = m('input[type=text]', {
          value: ctrl.caso().descricao(),
          onchange: m.withAttr('value', ctrl.caso().descricao)
        });
      }

      return m('#' + ctrl.caso().id + '.caso', {
        key: ctrl.caso().id
      }, [

        titulo,
        m.component(componente, {
          id: ctrl.caso().id,
          campos: ctrl.caso().campos
        })
      ]);
    }
  };
};
