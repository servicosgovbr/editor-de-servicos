'use strict';

var safeGet = require('utils/code-checks').safeGet;
var promise = require('utils/promise');

function botaoQueEspera(opts) {
  return m('button#' + opts.id, {
    onclick: opts.onclick,
    disabled: (opts.disabled ? 'disabled' : '')
  }, opts.disabled ? [m('i.fa.fa-spin.fa-spinner'), 'Despublicando...'] : [m('i.fa.fa-' + opts.icon), 'Despublicar']);
}

module.exports = {
  controller: function (args) {
    this.despublicar = safeGet(args, 'despublicar');
    this.despublicando = m.prop(false);

    this.onClick = function () {
      this.despublicando(true);
      m.redraw();
      return promise.onSuccOrError(this.despublicar(), _.bind(function () {
        this.despublicando(false);
        m.redraw();
      }, this));
    };
  },

  view: function (ctrl, args) {
    return m('#secao-despublicar', [
      m('hr'),
      botaoQueEspera({
        id: 'despublicar',
        onclick: _.bind(ctrl.onClick, ctrl),
        icon: '',
        disabled: ctrl.despublicando()
      }),
      m('hr')
    ]);
  }
};
