'use strict';

var safeGet = require('utils/code-checks').safeGet;

module.exports = {
  controller: function (args) {
    this.salvar = safeGet(args, 'salvar');

    this.salvando = m.prop(false);

    this.onClick = function () {
      this.salvando(true);
      return this.salvar().then(_.bind(function (resp) {
        this.salvando(false);
        alertify.success('Rascunho salvo com sucesso!');
        m.redraw();
        return resp;
      }, this), _.bind(function () {
        this.salvando(false);
        m.redraw();
      }, this));
    };
  },

  view: function (ctrl) {
    return m('button#salvar', {
      onclick: _.bind(ctrl.onClick, ctrl),
      disabled: ctrl.salvando() ? 'disabled' : ''
    }, ctrl.salvando() ? [
      m('i.fa.fa-spin.fa-spinner'),
      m.trust('&nbsp; Salvando...')
    ] : [
      m('i.fa.fa-floppy-o'),
      m.trust('&nbsp; Salvar')
    ]);
  }
};
