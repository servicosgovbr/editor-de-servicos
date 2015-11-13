'use strict';

var referencia = require('referencia');

module.exports = {
  view: function (ctrl, args) {
    if (!args.prop) {
      throw new Error('Necessário informar propriedade prop');
    }

    return m('div#area-de-interesse', m.component(require('componentes/select2'), {
      data: referencia.areasDeInteresse(),
      prop: args.prop,
      width: '100%',
      minimumResultsForSearch: 1,
      minimumInputLength: 0
    }));
  }
};
