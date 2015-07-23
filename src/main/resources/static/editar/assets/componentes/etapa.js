var Etapa = {

  controller: function(args) {
    this.etapa = args.etapa;
  },

  view: function(ctrl) {
    return m('.etapa', [
      m('.titulo', ctrl.etapa.titulo)
    ])
  }
};
