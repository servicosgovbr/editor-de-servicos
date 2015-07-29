'use strict';

module.exports = {

  controller: function (args) {
    this.metadados = (args || {}).metadados;
  },

  view: function (ctrl) {
    moment.locale('pt-br');

    if (!ctrl.metadados || !ctrl.metadados() || !ctrl.metadados().horario) {
      return m('');
    }

    return m('.metadados', {
      style: {
        fontSize: '12px',
        display: 'inline-block',
        marginTop: '10px',
        marginLeft: '165px'
      }
    }, [
      'Última revisão por ',
      ctrl.metadados().autor,
      ', ',
      moment(ctrl.metadados().horario).fromNow()
    ]);
  }
};
