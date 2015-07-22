var Legislacoes = {
  controller: function(args) {
    this.servico = args.servico;

    this.adicionar = function() {
      this.servico.legislacoes().push('');
    };

    this.remover = function(i) {
      this.servico.legislacoes().splice(i, 1);
    };
  },
  view: function(ctrl) {
    return m('', [
      m('h3', 'Legislações relacionadas ao serviço'),
      m('.legislacoes', ctrl.servico.legislacoes().map(function(legislacao, i) {
        return [
          m('input.inline.inline-xg[type=text]', {
            value: legislacao,
            onchange: function(e) {
              ctrl.servico.legislacoes()[i] = e.target.value;
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
