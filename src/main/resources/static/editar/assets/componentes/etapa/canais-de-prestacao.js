var etapa = etapa || {};

etapa.CanaisDePrestacao = {

  controller: function(args) {
    this.canaisDePrestacao = args.canaisDePrestacao;
  },

  view: function(ctrl) {
    return m('#' + ctrl.canaisDePrestacao().id, [
      m('h3', 'Canais de prestacao desta etapa'),

      m.component(new etapa.Caso(etapa.ListaDeCanaisDePrestacao), { padrao: true, caso: ctrl.canaisDePrestacao().casoPadrao }),
      m.component(new etapa.Casos(etapa.ListaDeCanaisDePrestacao), { casos: ctrl.canaisDePrestacao().outrosCasos })
    ]);
  }
};
