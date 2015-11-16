'use strict';

var Gratuidade = require('servico/modelos').Gratuidade;

module.exports = {
  view: function (ctrl, args) {
    var etapa = args.etapa;
    var gratuidade = args.gratuidade;
    var indice = args.indice;

    ctrl.adicionado = args.ctrl.adicionado;
    args.ctrl.adicionado = false;

    return m('fieldset#' + etapa.id, [

      m.component(require('servico/etapas/titulo'), {
        titulo: etapa.titulo,
        indice: indice,
        adicionado: ctrl.adicionado
      }),

      m.component(require('servico/etapas/descricao'), {
        descricao: etapa.descricao,
        indice: indice
      }),

      m.component(require('servico/etapas/documentos'), {
        documentos: etapa.documentos,
        indice: indice,
      }),

      gratuidade() === Gratuidade.GRATUITO ? null : m.component(require('servico/etapas/custos'), {
        custos: etapa.custos,
        indice: indice,
      }),

      m.component(require('servico/etapas/canais-de-prestacao'), {
        canaisDePrestacao: etapa.canaisDePrestacao,
        indice: indice,
      }),
    ]);
  }
};
