var SegmentosDaSociedade = {

  controller: function(args) {
    this.segmentosDaSociedade = args.segmentosDaSociedade;

    this.adicionar = function(e) {
      var segmento = e.target.value;
      var segmentos = this.segmentosDaSociedade();

      segmentos = _.without(segmentos, segmento);
      if (e.target.checked) {
        segmentos.push(segmento);
      }

      this.segmentosDaSociedade(segmentos);
    };

    this.todosOsSegmentosDaSociedade = m.request({ method: 'GET', url: '/editar/api/segmentos-da-sociedade' });
  },

  view: function(ctrl) {
    return m('fieldset#segmentos-da-sociedade', [
      m("h3", "Segmentos da sociedade"),
      m("", ctrl.todosOsSegmentosDaSociedade().map(function(segmento) {
        return m('label', [
          m("input[type=checkbox]", {
            value: segmento,
            checked: _.contains(ctrl.segmentosDaSociedade(), segmento),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          segmento
        ]);
      }))
    ]);
  }
};
