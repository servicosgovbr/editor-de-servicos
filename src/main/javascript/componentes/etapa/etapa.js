'use strict';

module.exports = {

  controller: function (args) {
    this.etapa = args.etapa;
    this.gratuidade = args.gratuidade;
  },

  view: function (ctrl) {
    return m('.etapa#' + ctrl.etapa.id, [
      m.component(require('componentes/etapa/titulo'), {
        titulo: ctrl.etapa.titulo
      }),
      m.component(require('componentes/etapa/descricao'), {
        descricao: ctrl.etapa.descricao
      }),
      m.component(require('componentes/etapa/documentos'), {
        documentos: ctrl.etapa.documentos
      }),
      ctrl.gratuidade() ? null : m.component(require('componentes/etapa/custos'), {
        custos: ctrl.etapa.custos
      }),
      m.component(require('componentes/etapa/canais-de-prestacao'), {
        canaisDePrestacao: ctrl.etapa.canaisDePrestacao
      }),
    ]);
  }
};
