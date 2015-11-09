'use strict';

var safeGet = require('utils/code-checks').safeGet;
var avisos = require('utils/avisos');
var promise = require('utils/promise');

module.exports = {
  controller: function(args) {

    this.publicar = safeGet(args, 'publicar');
    this.publicando = m.prop(false);

    this.publicarClick = function () {
      this.publicando(true);
      m.redraw();

      promise.onSuccOrError(
        this.publicar()
          .then(
            avisos.sucessoFn('Serviço publicado com sucesso!'),
            avisos.erroFn('Serviço ainda contém erros.')),
        _.bind(function() {
          this.publicando(false);
          m.redraw();
        }, this)
      );
    };

    this.rejeitarClick = function () {

    };

  },

  view: function(ctrl) {
    return m('button#publicar', {
      onclick: _.bind(ctrl.publicarClick, ctrl),
      disabled: ctrl.publicando() ? 'disabled' : ''
    }, ctrl.publicando() ? [
      m('i.fa.fa-spin.fa-spinner'),
      m.trust('&nbsp; Publicando...')
    ] : [
      m('i.fa.fa-tv'),
       m.trust('&nbsp; Publicar')
    ]);
  }

};

