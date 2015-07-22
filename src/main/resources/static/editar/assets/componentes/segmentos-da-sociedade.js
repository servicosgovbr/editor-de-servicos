var SegmentosDaSociedade = {
  controller: function(args) {
    this.servico = args.servico;

    this.adicionar = function(e) {
      var segmento = e.target.value;
      var segmentos = this.servico.segmentosDaSociedade();

      segmentos = _.without(segmentos, segmento);
      if (e.target.checked) {
        segmentos.push(segmento);
      }

      this.servico.segmentosDaSociedade(segmentos);
    };

    this.segmentosDaSociedade = m.request({ method: 'GET', url: '/editar/api/segmentos-da-sociedade' });
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Segmentos da sociedade"),
      m("", ctrl.segmentosDaSociedade().map(function(segmento) {
        return m('label', [
          m("input[type=checkbox]", {
            value: segmento,
            checked: _.contains(ctrl.servico.segmentosDaSociedade(), segmento),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          segmento
        ]);
      }))
    ]);
  }
};
