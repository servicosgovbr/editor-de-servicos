/*global loggedUser*/
'use strict';

var safeGet = require('utils/code-checks').safeGet;
var permissoes = require('utils/permissoes');

module.exports = {
  controller: function (args) {
    this.salvar = safeGet(args, 'salvar');
    this.salvandoServico = args.salvandoServico;
    this.caiuSessao = args.caiuSessao;
    this.orgaoId = args.orgaoId;

    this.salvando = m.prop(false);

    this.onClick = function () {
      this.salvando(true);
      this.salvandoServico(true);
      return this.salvar().then(_.bind(function (resp) {
        this.salvando(false);
        this.salvandoServico(false);
        alertify.success('Rascunho salvo com sucesso!', 0);
        m.redraw();
        return resp;
      }, this), _.bind(function (msg) {
        alertify.error(msg, 0);
        this.salvando(false);
        this.salvandoServico(false);
        m.redraw();
      }, this));
    };
  },

  view: function (ctrl) {
    return permissoes.podeSalvarPagina() ? m('button#salvar', {
      onclick: _.bind(ctrl.onClick, ctrl),
      disabled: ctrl.salvando() || ctrl.salvandoServico() || ctrl.caiuSessao() ? 'disabled' : ''
    }, ctrl.salvando() ? [
      m('i.fa.fa-spin.fa-spinner'),
      m.trust('&nbsp; Salvando...')
    ] : [
      m('i.fa.fa-floppy-o'),
      m.trust('&nbsp; Salvar')
    ]) : m('');
  }
};
