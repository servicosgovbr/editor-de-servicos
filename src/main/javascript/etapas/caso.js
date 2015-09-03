'use strict';

var focus = require('focus');

module.exports = function (componente) {

  return {
    view: function (ctrl, args) {
      var caso = args.caso;
      var padrao = !!args.padrao;
      var titulo = args.titulo;
      var opcional = args.opcional;
      ctrl.adicionado = caso().adicionado;

      var inputNome;
      var className;
      if (padrao) {
        inputNome = '';
        className = '';
      } else {
        inputNome = m('div.input-container', {
          class: caso().descricao.erro()
        }, [
          m('input[type=text]', {
            value: caso().descricao(),
            config: focus(ctrl),
            onchange: m.withAttr('value', caso().descricao)
          })
        ]);
        className = '.margin-left';
      }

      return m('#' + caso().id + '.caso' + className, {
        key: caso().id
      }, [

        inputNome,
        m('label.titulo', {
          class: opcional ? 'opcional' : ''
        }, titulo),
        m.component(componente, {
          id: caso().id,
          campos: caso().campos
        })
      ]);
    }
  };
};
