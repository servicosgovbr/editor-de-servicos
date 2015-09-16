'use strict';

var referencia = require('referencia');
var tooltips = require('tooltips');

module.exports = {

  controller: function (args) {
    this.pagina = args.pagina;
  },

  view: function (ctrl, args) {

    var componenteTipo = args.novo ? m.component(require('componentes/select2'), {
      prop: ctrl.pagina().tipo,
      data: referencia.tiposDePagina
    }) : referencia.tipoDePagina(ctrl.pagina().tipo());

    return m('fieldset#tipoDePagina', [
            m('h3', [
                'Tipo de Página: ',
                componenteTipo,
                args.novo ? m.component(tooltips.tipoPagina) : ''
            ])
        ]);
  }
};
