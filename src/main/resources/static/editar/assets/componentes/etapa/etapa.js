var etapa = etapa || {};

var Etapa = {

  controller: function(args) {
    this.etapa = args.etapa;
    this.gratuidade = args.gratuidade;
  },

  view: function(ctrl) {
    return m('.etapa#' + ctrl.etapa.id, [
      m.component(etapa.Titulo, { titulo: ctrl.etapa.titulo }),
      m.component(etapa.Descricao, { descricao: ctrl.etapa.descricao }),
      m.component(etapa.Documentos , { documentos: ctrl.etapa.documentos }),
      ctrl.gratuidade() ? null : m.component(etapa.Custos, { custos: ctrl.etapa.custos }),
      m.component(etapa.CanaisDePrestacao, { canaisDePrestacao: ctrl.etapa.canaisDePrestacao }),
    ])
  }
};
