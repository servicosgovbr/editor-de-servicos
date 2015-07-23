var etapa = etapa || {};

etapa.Caso = function(componente) {
  return {
    controller: function(args) {
      this.caso = args.caso;
      this.padrao = args.padrao || false;
    },

    view: function(ctrl) {
      var titulo;
      if (ctrl.padrao) {
        titulo = m('h4', ctrl.caso().descricao());
      } else {
        titulo = m('input[type=text]', {
          value: ctrl.caso().descricao(),
          onchange: m.withAttr('value', ctrl.caso().descricao)
        });
      }

      return m('#' + ctrl.caso().id, [
        titulo,
        m.component(componente, { campos: ctrl.caso().campos })
      ])
    }
  };
}