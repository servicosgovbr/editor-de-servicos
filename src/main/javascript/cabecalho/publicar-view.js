'use strict';

var safeGet = require('utils/code-checks').safeGet;
var avisos = require('utils/avisos');
var promise = require('utils/promise');

function botaoQueEspera(flagProp, opts) {
  return m('button#' + opts.id, {
    onclick: opts.onclick,
    disabled: (opts.disabled || flagProp() ? 'disabled' : '')
  }, flagProp() ? m('i.fa.fa-spin.fa-spinner') : m('i.fa.fa-' + opts.icon));
}

module.exports = {

  controller: function (args) {
    this.publicar = safeGet(args, 'publicar');
    this.descartar = safeGet(args, 'descartar');

    this.publicando = m.prop(false);
    this.descartando = m.prop(false);

    this.opera = function (prop, operacao) {
      prop(true);
      m.redraw();

      promise.onSuccOrError(
        operacao,
        _.bind(function () {
          prop(false);
          m.redraw();
        }, this));
    };

    this.publicarClick = function () {
      this.opera(
        this.publicando,
        this.publicar()
        .then(
          avisos.sucessoFn('Serviço publicado com sucesso!'),
          avisos.erroFn('Serviço ainda contém erros.')));
    };

    this.descartarClick = function () {
      this.opera(
        this.descartando,
        this.descartar()
        .then(
          avisos.erroFn('Alterações rejeitadas, recarregando serviço'),
          avisos.erroFn('Não foi possível descartar as alterações')));
    };
  },

  view: function (ctrl, args) {
    var meta = args.metadados;

    var desabilitaBotoes = ctrl.publicando() || ctrl.descartando();

    function podeDescartar() {
      return _.get(meta, 'editado.revisao') && (_.get(meta, 'editado.revisao') !== _.get(meta, 'publicado.revisao'));
    }

    function podePublicar() {
      return podeDescartar();
    }


    return m('span#publicar-view', [
      m('span.label-botao','Publicar alterações?'),
      m.trust('&nbsp&nbsp'),
      botaoQueEspera(ctrl.descartando, {
        id: 'descartar',
        onclick: _.bind(ctrl.descartarClick, ctrl),
        icon: 'times',
        disabled: desabilitaBotoes || !podeDescartar()
      }),

      botaoQueEspera(ctrl.publicando, {
        id: 'publicar',
        onclick: _.bind(ctrl.publicarClick, ctrl),
        icon: 'check',
        disabled: desabilitaBotoes || !podePublicar()
      })
    ]);
  }
};
