'use strict';

var focus = require('focus');

module.exports = function (componente) {

  return {

    controller: function (args) {
      this.caso = args.caso;
      this.padrao = !!args.padrao;
      this.titulo = args.titulo;
      this.opcional = args.opcional;
      this.adicionado = this.caso().adicionado;
    },

    view: function (ctrl, args) {
      var inputNome;
      var className;
      if (ctrl.padrao) {
        inputNome = '';
        className = '';
      } else {
        inputNome = m('div.input-container', {
          class: ctrl.caso().descricao.erro()
        }, [
          m('input[type=text]', {
            value: ctrl.caso().descricao(),
            config: focus(ctrl),
            onchange: m.withAttr('value', ctrl.caso().descricao)
          })
        ]);
        className = '.margin-left';
      }

      return m('#' + ctrl.caso().id + '.caso' + className, {
        key: ctrl.caso().id
      }, [

        inputNome,
        m('label.titulo', {
          class: ctrl.opcional ? 'opcional' : ''
        }, ctrl.titulo),
        m.component(componente, {
          id: ctrl.caso().id,
          campos: ctrl.caso().campos
        })
      ]);
    }
  };
};
