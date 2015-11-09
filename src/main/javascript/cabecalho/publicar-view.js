'use strict';

var safeGet = require('utils/code-checks').safeGet;
var avisos = require('utils/avisos');
var promise = require('utils/promise');

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
    return m('span', [
      'Publicar alterações?',

      m('button#descartar', {
        onclick: _.bind(ctrl.descartarClick, ctrl),
        disabled: ctrl.operando() ? 'disabled' : ''
      }, 'N'),

      m('button#publicar', {
        onclick: _.bind(ctrl.publicarClick, ctrl),
        disabled: ctrl.operando() ? 'disabled' : ''
      }, 'Y')

    ]);

  }

};
