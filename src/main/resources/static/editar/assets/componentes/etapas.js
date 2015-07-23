var Etapas = {

  controller: function(args) {
    this.etapas = args.etapas;

    this.adicionar = function() {
      this.etapas.push(new models.Etapa());
    };

    this.remover = function(i) {
      this.etapas.splice(i, 1);
    };
  },

  view: function(ctrl) {
    return m('fieldset#etapas', [
      m('h3', 'Etapas'),

      ctrl.etapas.map(function(etapa, i) {
        return m('span', {
          key: etapa.id
        }, [
          m.component(Etapa, { etapa: etapa }),

          m('button', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m("span.fa.fa-times"), ' Remover etapa '
          ])
        ]);
      }),

      m("button.adicionar-etapa", {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m("i.fa.fa-plus"),
        " Adicionar etapa "
      ])
    ])
  }
};