'use strict';

var referencia = require('referencia');

module.exports = {
  view: function (ctrl, args) {
    var tooltipTipo = args.tooltipTipo;

    var componenteTipo = args.novo ? m.component(require('componentes/select2'), {
      prop: args.tipo,
      change: args.change,
      data: referencia.tiposDePagina
    }) : referencia.tipoDePagina(args.tipo());

    return m('fieldset#tipoDePagina', [
      m('h3', [
        'Tipo de Página',
        args.novo ? m.component(tooltipTipo) : '',
        componenteTipo
      ])
    ]);
  }
};
