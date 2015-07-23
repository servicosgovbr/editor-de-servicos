var etapa = etapa || {};

etapa.Custos = {

  controller: function(args) {
    this.custos = args.custos;
  },

  view: function(ctrl) {
    return m('#' + ctrl.custos().id, [
      m('h3', 'Custos desta etapa'),

      m.component(new etapa.Caso(etapa.ListaDeCustos), { padrao: true, caso: ctrl.custos().casoPadrao }),
      m.component(new etapa.Casos(etapa.ListaDeCustos), { casos: ctrl.custos().outrosCasos })
    ]);
  }
};
