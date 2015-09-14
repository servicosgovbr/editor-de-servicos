'use strict';

module.exports = {

  view: function (ctrl, args) {

    var binding = args;

    return m('#dados-basicos', [
      m.component(require('./nome'), binding),
      m.component(require('./sigla'), binding),
      m.component(require('./nomes-populares'), binding),
      m.component(require('./descricao'), binding),
      m.component(require('./tempo-total-estimado'), binding),
      m.component(require('./gratuidade'), binding),
    ]);

  }

};
