'use strict';

var referencia = require('referencia');

module.exports = {
  view: function (ctrl, args) {
    var tooltipTipo = args.tooltipTipo;

    var componenteTipo = args.novo ? m.component(require('componentes/select2'), {
      prop: args.pagina().tipo,
      data: referencia.tiposDePagina
    }) : referencia.tipoDePagina(args.pagina().tipo());

    return m('fieldset#tipoDePagina', [
      m('h3', [
        'Tipo de Página: ',
        componenteTipo,
        args.novo ? m.component(tooltipTipo) : ''
      ])
    ]);
  }
};
