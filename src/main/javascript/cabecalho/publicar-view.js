'use strict';

var safeGet = require('utils/code-checks').safeGet;
var avisos = require('utils/avisos');
var promise = require('utils/promise');

function botaoQueEspera(flagProp, opts) {
  return m('button#' + opts.id, {
      onclick: opts.onclick,
      disabled: flagProp() ? 'disabled' : ''
    }, flagProp() ? m('i.fa.fa-spin.fa-spinner')
     : m('i.fa.fa-' + opts.icon)
  );
}

module.exports = {
  controller: function (args) {

    this.publicar = safeGet(args, 'publicar');
    this.descartar = safeGet(args, 'descartar');

    this.operando = m.prop(false);

    this.opera = function (operacao) {
      this.operando(true);
      m.redraw();

      promise.onSuccOrError(
        operacao,
        _.bind(function () {
          this.operando(false);
          m.redraw();
        }, this));
    };

    this.publicarClick = function () {
      this.opera(
        this.publicar()
        .then(
          avisos.sucessoFn('Serviço publicado com sucesso!'),
          avisos.erroFn('Serviço ainda contém erros.')));
    };

    this.descartarClick = function () {
      this.opera(
        this.descartar()
        .then(
          avisos.sucessoFn('Alterações rejeitadas, recarregando dados de serviço'),
          avisos.erroFn('Não foi possível descartar as alterações')));
    };

  },

  view: function (ctrl) {
    return m('span#publicar-view', [
      'Publicar alterações?',
      m.trust('&nbsp&nbsp'),
      botaoQueEspera(ctrl.operando, {
        id: 'descartar',
        onclick: _.bind(ctrl.descartarClick, ctrl),
        icon: 'times'
      }),
      botaoQueEspera(ctrl.operando, {
        id: 'publicar',
        onclick: _.bind(ctrl.publicarClick, ctrl),
        icon: 'check'
      })
    ]);
  }

};
