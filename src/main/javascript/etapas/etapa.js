'use strict';

module.exports = {

  controller: function (args) {
    this.etapa = args.etapa;
    this.gratuidade = args.gratuidade;
    this.indice = args.indice;
    this.adicionado = args.ctrl.adicionado;
    args.ctrl.adicionado = false;
  },

  view: function (ctrl) {
    ctrl.etapa.validar();
    var validador = ctrl.etapa.validador;

    return m('fieldset#' + ctrl.etapa.id, [

      m.component(require('etapas/titulo'), {
        titulo: ctrl.etapa.titulo,
        indice: ctrl.indice,
        adicionado: ctrl.adicionado,
        erro: validador.hasError('titulo')
      }),

      m.component(require('etapas/descricao'), {
        descricao: ctrl.etapa.descricao,
        indice: ctrl.indice,
        erro: validador.hasError('descricao')
      }),

      m.component(require('etapas/documentos'), {
        documentos: ctrl.etapa.documentos,
        indice: ctrl.indice,
        erros: validador.hasError('documentos')
      }),

      ctrl.gratuidade() ? null : m.component(require('etapas/custos'), {
        custos: ctrl.etapa.custos,
        indice: ctrl.indice,
        erros: validador.hasError('custos')
      }),

      m.component(require('etapas/canais-de-prestacao'), {
        canaisDePrestacao: ctrl.etapa.canaisDePrestacao,
        indice: ctrl.indice
      }),
    ]);
  }
};
