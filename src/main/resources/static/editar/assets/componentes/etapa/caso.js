var etapa = etapa || {};

etapa.Caso = function(componente) {
  return {
    controller: function(args) {
      this.caso = args.caso;
    },

    view: function(ctrl) {
      return m('#' + ctrl.caso().id, [
        m('h4', 'Para todos os casos'),
        m.component(componente, { campos: ctrl.caso().campos })
      ])
    }
  };
}