var etapa = etapa || {};

etapa.Documentos = {

  controller: function(args) {
    this.documentos = args.documentos;
  },

  view: function(ctrl) {
    return m('#' + ctrl.documentos().id, [
      m('h3', 'Documentação necessária'),

      m.component(new etapa.Caso(etapa.ListaDeDocumentos), { padrao: true, caso: ctrl.documentos().casoPadrao }),
      m.component(new etapa.Casos(etapa.ListaDeDocumentos), { casos: ctrl.documentos().outrosCasos })
    ]);
  }

}