'use strict';

var safeGet = require('utils/code-checks').safeGet;

module.exports = {
  controller: function (args) {
    this.visualizar = safeGet(args, 'visualizar');

    this.onClick = function () {
      this.visualizar();
    };
  },

  view: function (ctrl, args) {
    var meta = args.metadados;

    function temEdicao() {
      return _.get(meta, 'editado.revisao');
    }

    return m('button#visualizar', {
      onclick: _.bind(ctrl.onClick, ctrl),
      disabled: _.contains('/editar/servico/novo', m.route()) && !temEdicao()
    }, [
      m('i.fa.fa-eye'),
      m.trust('&nbsp; Visualizar')
    ]);
  }
};
