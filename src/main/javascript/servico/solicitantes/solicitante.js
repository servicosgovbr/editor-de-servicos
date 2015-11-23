'use strict';

var focus = require('focus');

module.exports = {
  controller: function (args) {
    this.adicionado = false;
  },

  view: function (ctrl, args) {
    var solicitante = args.solicitante;
    var index = args.index;
    var showDelete = args.showDelete;
    var removerFn = args.remover;
    ctrl.adicionado = args.adicionado;

    return m('fieldset#' + solicitante.id + '.relative', {
      key: solicitante.id
    }, [

      m('h3', [
        'Tipo do solicitante ' + (index + 1),
        m.component(require('tooltips').tipoSolicitante)]),

      (showDelete ? m('button.remove.absolute', {
        onclick: removerFn
      }) : ''),

        m('div.input-container', {
          class: solicitante.tipo.erro()
        },
        m('input[type=text]', {
          value: solicitante.tipo(),
          config: focus(ctrl),
          onchange: m.withAttr('value', solicitante.tipo)
        })
        ),

        m('h3.opcional', [
          'Requisitos necessários para o solicitante ' + (index + 1),
          m.component(require('tooltips').requisitosSolicitante)
        ]),

        m.component(require('componentes/editor-markdown'), {
        rows: 3,
        value: solicitante.requisitos(),
        onchange: m.withAttr('value', solicitante.requisitos),
        erro: solicitante.requisitos.erro()
      })

    ]);
  }
};
