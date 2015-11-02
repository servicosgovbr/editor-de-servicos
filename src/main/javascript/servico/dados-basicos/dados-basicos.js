'use strict';

module.exports = {

  view: function (ctrl, args) {
    return m('#dados-basicos', [
      m.component(require('./nome'), args),
      m.component(require('./sigla'), args),
      m.component(require('./nomes-populares'), args),
      m.component(require('./descricao'), args),
      m.component(require('./tempo-total-estimado'), args),
      m.component(require('./gratuidade'), args),
    ]);

  }

};
