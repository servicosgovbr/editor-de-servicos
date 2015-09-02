'use strict';

var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().nomesPopulares().push('');
      this.adicionado = true;
    };

    this.remover = function (i) {
      this.servico().nomesPopulares().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#nomes-populares.relative', [
      m('h3.opcional', [
        'Nomes populares',
        m.component(require('tooltips').nomesPopulares)
      ]),

      ctrl.servico().nomesPopulares().map(function (nomesPopulares, i) {
        return [
          ctrl.servico().nomesPopulares().length === 1 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m('div.input-container', {
            class: (ctrl.servico().nomesPopulares.erro() || [])[i]
          }, [
            m('input.inline[type=text]', {
              value: nomesPopulares,
              config: focus(ctrl),
              onchange: function (e) {
                ctrl.servico().nomesPopulares()[i] = e.target.value;
              }
            })
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
