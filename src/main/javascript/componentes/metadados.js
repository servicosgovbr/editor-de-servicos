'use strict';

module.exports = {

  controller: function (args) {
    this.metadados = (args || {}).metadados;
  },

  view: function (ctrl) {
    moment.locale('pt-br');

    if (!ctrl.metadados || !ctrl.metadados() || !ctrl.metadados().horario) {
      return m('.metadados', m.trust('&nbsp;'));
    }

    return m('.metadados', [
      'Salvo por ',
      ctrl.metadados().autor,
      ', ',
      moment(ctrl.metadados().horario).fromNow()
    ]);
  }
};
