var etapa = etapa || {};

etapa.Casos = function(componente)  {

  return {

    controller: function(args) {
      this.casos = args.casos;

      this.adicionar = function () {
        this.casos().push(new models.Caso());
      };
      this.remover = function(i) {
        this.casos().splice(i, 1);
      }
    },

    view: function(ctrl) {
      return m('', [
        ctrl.casos().map(function(caso, i) {
          return [
            m.component(new etapa.Caso(componente), { caso: m.prop(caso) }),
            m('button.inline', {
              onclick: ctrl.remover.bind(ctrl, i)
            }, [
              m("span.fa.fa-times"),
              ' Remover caso '
            ])
          ]
        }),
        m('button.adicionar-caso', {
          onclick: ctrl.adicionar.bind(ctrl)
        }, [
          m('i.fa.fa-indent'),
          ' Adicionar caso '
        ])
      ]);
    }
  }
}