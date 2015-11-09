'use strict';

var safeGet = require('utils/code-checks').safeGet;

module.exports = {
  controller: function(args) {
    this.editar = safeGet(args, 'editar');

    this.onClick = function () {
      this.editar();
    };
  },

  view: function(ctrl) {
    return m('button#editar', {
      onclick: _.bind(ctrl.onClick, ctrl)
    }, [
      m('i.fa.fa-pencil'),
      m.trust('&nbsp; Editar')
    ]);
  }
};
