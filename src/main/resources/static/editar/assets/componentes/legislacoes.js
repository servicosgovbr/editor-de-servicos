var Legislacoes = {

  controller: function(args) {
    this.legislacoes = args.legislacoes;

    this.adicionar = function() {
      this.legislacoes().push('');
    };

    this.remover = function(i) {
      this.legislacoes().splice(i, 1);
    };
  },

  view: function(ctrl) {
    return m('', [
      m('h3', 'Legislações relacionadas ao serviço'),
      m('.legislacoes', ctrl.legislacoes().map(function(legislacao, i) {
        return [
          m('input.inline.inline-xg[type=text]', {
            value: legislacao,
            onchange: function(e) {
              ctrl.legislacoes()[i] = e.target.value;
            }
          }),
          m('button.inline.remove-peq', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m("span.fa.fa-times")
          ])
        ];
      })),
      m('button.adicionar-legislacao', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m("i.fa.fa-plus"),
        " Adicionar legislação "
      ])
    ]);
  }
};
