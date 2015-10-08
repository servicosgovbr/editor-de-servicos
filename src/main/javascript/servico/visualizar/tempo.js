'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;
  },

  view: function (ctrl) {
    return m('', 'Tempo');
  }
};
