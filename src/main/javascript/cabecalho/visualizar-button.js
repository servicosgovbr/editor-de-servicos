'use strict';

var safeGet = require('utils/code-checks').safeGet;

module.exports = {
  controller: function(args) {
    this.visualizar = safeGet(args, 'visualizar');

    this.onClick = function () {
      this.visualizar();
    };
  },

  view: function(ctrl) {
    return m('button#visualizar', {
      onclick: _.bind(ctrl.onClick, ctrl),
    }, [
      m('i.fa.fa-eye'),
      m.trust('&nbsp; Visualizar')
    ]);
  }
};

