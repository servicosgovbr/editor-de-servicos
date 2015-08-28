'use strict';

var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.solicitante = args.solicitante;
  },

  view: function (ctrl, args) {
    var showDelete = args.showDelete;
    var removerFn = args.remover;

    var s = ctrl.solicitante;

    s.validar();
    var validador = s.validador;

    return m('fieldset#' + s.id + '.relative', {
      key: s.id
    }, [

      m('h3', [
        'Tipo de solicitante',
        m.component(require('tooltips').tipoSolicitante)]),

      (showDelete ? m('button.remove.absolute', {
        onclick: removerFn
      }) : ''),

        m('div.input-container', {
          class: validador.hasError('tipo')
        },
        m('input[type=text]', {
          value: s.tipo(),
          config: focus(ctrl),
          onchange: m.withAttr('value', s.tipo)
        })
        ),

        m('h3.opcional', [
          'Requisitos necessários para o solicitante',
          m.component(require('tooltips').requisitosSolicitante)
        ]),

        m.component(require('componentes/editor-markdown'), {
        rows: 3,
        value: s.requisitos(),
        onchange: m.withAttr('value', s.requisitos),
        erro: validador.hasError('requisitos')
      })

    ]);
  }
};
