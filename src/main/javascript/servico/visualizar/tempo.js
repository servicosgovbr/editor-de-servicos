'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;

    this.temTempo = function() {
        return true;
    };
  },

  view: function (ctrl) {
      if (ctrl.temTempo()) {
          return m('#servico-tempo', []);
      }
      return m.component(require('servico/visualizar/view-vazia'));
  }
};
