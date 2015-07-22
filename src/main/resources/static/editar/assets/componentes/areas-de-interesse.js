var AreasDeInteresse = {
  controller: function(args) {
    this.servico = args.servico;
    this.areasDeInteresse = m.request({ method: 'GET', url: '/editar/api/areas-de-interesse' });

    this.adicionar = function(e) {
      var area = e.target.value;
      var areas = this.servico.areasDeInteresse();

      areas = _.without(areas, area);
      if (e.target.checked) {
        areas.push(area);
      }
      this.servico.areasDeInteresse(areas);
    }
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Áreas de interesse"),
      m("", ctrl.areasDeInteresse().map(function(area) {
        return m('label', [
          m("input[type=checkbox]", {
            value: area,
            checked: _.contains(ctrl.servico.areasDeInteresse(), area),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          area
        ]);
      }))
    ]);
  }
};