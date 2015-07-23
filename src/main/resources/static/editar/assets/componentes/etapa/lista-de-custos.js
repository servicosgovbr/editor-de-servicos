var etapa = etapa || {};

etapa.ListaDeCustos = {

  controller: function(args) {
    this.custos = args.campos;
  },

  view: function(ctrl) {
    return m('.custos', [
      ctrl.custos().map(function(custo) {
        return m('.custo', [custo.descricao(), custo.moeda(), custo.valor()])
      })
    ]);
  }
};
