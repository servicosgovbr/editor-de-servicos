'use strict';

module.exports = {

  controller: function (args) {
    this.etapa = args.etapa;
    this.gratuidade = args.gratuidade;
    this.indice = args.indice;
  },

  view: function (ctrl) {
    return m('fieldset#' + ctrl.etapa.id, [
      m.component(require('componentes/etapa/titulo'), {
        titulo: ctrl.etapa.titulo,
        indice: ctrl.indice
      }),
      m.component(require('componentes/etapa/descricao'), {
        descricao: ctrl.etapa.descricao,
        indice: ctrl.indice

      }),
      m.component(require('componentes/etapa/documentos'), {
        documentos: ctrl.etapa.documentos,
        indice: ctrl.indice

      }),
      ctrl.gratuidade() ? null : m.component(require('componentes/etapa/custos'), {
        custos: ctrl.etapa.custos,
        indice: ctrl.indice

      }),
      m.component(require('componentes/etapa/canais-de-prestacao'), {
        canaisDePrestacao: ctrl.etapa.canaisDePrestacao,
        indice: ctrl.indice
      }),
    ]);
  }
};
