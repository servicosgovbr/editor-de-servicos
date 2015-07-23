var etapa = etapa || {};

var Etapa = {

  controller: function(args) {
    this.etapa = args.etapa;
  },

  view: function(ctrl) {
    return m('.etapa', [
      m.component(etapa.Titulo, { titulo: ctrl.etapa.titulo }),
      m.component(etapa.Descricao, { descricao: ctrl.etapa.descricao }),
    ])
  }
};
