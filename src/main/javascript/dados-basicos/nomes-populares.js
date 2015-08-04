'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().nomesPopulares().push('');
    };

    this.remover = function (i) {
      this.servico().nomesPopulares().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#nomes-populares', [
      m('h3', [
        'Nomes populares',
        m.component(require('tooltips').nomesPopulares)
      ]),

      ctrl.servico().nomesPopulares().map(function (nomesPopulares, i) {
        return [
          m('input.inline[type=text]', {
            value: nomesPopulares,
            onchange: function (e) {
              ctrl.servico().nomesPopulares()[i] = e.target.value;
            }
          }),
          m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-trash-o')
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
