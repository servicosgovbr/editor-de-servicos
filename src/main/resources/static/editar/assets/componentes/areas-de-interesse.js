var AreasDeInteresse = {

  controller: function(args) {
    this.areasDeInteresse = args.areasDeInteresse;
    this.todasAreasDeInteresse = m.request({ method: 'GET', url: '/editar/api/areas-de-interesse' });

    this.adicionar = function(e) {
      var area = e.target.value;
      var areas = this.areasDeInteresse();

      areas = _.without(areas, area);
      if (e.target.checked) {
        areas.push(area);
      }
      this.areasDeInteresse(areas);
    }
  },

  view: function(ctrl) {
    return m('', [
      m("h3", "Áreas de interesse"),
      m("", ctrl.todasAreasDeInteresse().map(function(area) {
        return m('label', [
          m("input[type=checkbox]", {
            value: area,
            checked: _.contains(ctrl.areasDeInteresse(), area),
            onchange: ctrl.adicionar.bind(ctrl)
          }),
          area
        ]);
      }))
    ]);
  }
};