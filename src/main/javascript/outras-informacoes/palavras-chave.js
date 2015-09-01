'use strict';

var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().palavrasChave().push('');
      this.adicionado = true;
    };

    this.remover = function (i) {
      this.servico().palavrasChave().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#palavras-chave.relative', [
      m('h3', [
        'Palavras-chave',
        m.component(require('tooltips').palavrasChave)
      ]),

      ctrl.servico().palavrasChave().map(function (palavras, i) {
        return [
          ctrl.servico().palavrasChave().length === 1 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m('.input-container', {
            class: ctrl.servico().palavrasChave.erro()[i]
          }, [
            m('input.inline[type=text]', {
              value: palavras,
              config: focus(ctrl),
              onchange: function (e) {
                ctrl.servico().palavrasChave()[i] = e.target.value;
              }
            })
          ])
        ];
      }),

      m('button.adicionar.adicionar-palavra-chave', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar palavra-chave '
      ])
    ]);
  }
};
