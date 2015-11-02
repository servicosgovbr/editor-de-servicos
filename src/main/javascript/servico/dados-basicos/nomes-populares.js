'use strict';

var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      var nomesPopulares = this.servico().nomesPopulares();
      nomesPopulares.push('');

      this.servico().nomesPopulares(nomesPopulares);
      this.adicionado = true;
    };

    this.remover = function (i) {
      var nomesPopulares = this.servico().nomesPopulares();
      nomesPopulares.splice(i, 1);
      this.servico().nomesPopulares(nomesPopulares);
    };
  },

  view: function (ctrl, args) {
    ctrl.servico = args.servico;

    return m('fieldset#nomes-populares.relative', [
      m('h3.opcional', [
        'Nomes populares',
        m.component(require('tooltips').nomesPopulares)
      ]),

      ctrl.servico().nomesPopulares().map(function (nomePopular, i) {
        return [
          ctrl.servico().nomesPopulares().length === 1 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m('div.input-container', {
            class: (ctrl.servico().nomesPopulares.erro() || [])[i]
          }, [
            m('input.inline[type=text]', {
              value: nomePopular,
              config: focus(ctrl),
              onchange: function (e) {
                var nomesPopulares = ctrl.servico().nomesPopulares();
                nomesPopulares[i] = e.target.value;
                ctrl.servico().nomesPopulares(nomesPopulares);
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
