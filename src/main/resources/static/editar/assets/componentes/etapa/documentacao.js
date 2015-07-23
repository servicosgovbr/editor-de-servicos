var etapa = etapa || {};

etapa.Documentacao = {

  controller: function(args) {
    this.documentacao = args.documentacao;
  },

  view: function(ctrl) {
    return m('#' + ctrl.documentacao().id, [
      m('h3', 'Documentação necessária'),

      m.component(new etapa.Caso(etapa.Documentos), { padrao: true, caso: ctrl.documentacao().casoPadrao }),
      m.component(new etapa.Casos(etapa.Documentos), { casos: ctrl.documentacao().outrosCasos })
    ]);
  }

}