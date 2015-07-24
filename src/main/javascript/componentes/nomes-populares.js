'use strict';

module.exports = {

  controller: function (args) {
    this.nomesPopulares = args.nomesPopulares;

    this.adicionar = function () {
      this.nomesPopulares().push('');
    };

    this.remover = function (i) {
      this.nomesPopulares().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#nomes-populares', [
      m('h3', 'Nomes populares'),

      ctrl.nomesPopulares().map(function (legislacao, i) {
        return [
          m('input.inline.inline-xg[type=text]', {
            value: legislacao,
            onchange: function (e) {
              ctrl.nomesPopulares()[i] = e.target.value;
            }
          }),
          m('button.inline.remove-peq', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-times')
          ])
        ];
      }),

      m('button.adicionar.adicionar-nome-popular', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar nome popular '
      ])
    ]);
  }
};
