'use strict';

var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      var palavrasChave = this.servico().palavrasChave();
      palavrasChave.push('');

      this.servico().palavrasChave(palavrasChave);
      this.adicionado = true;
    };

    this.remover = function (i) {
      var palavrasChave = this.servico().palavrasChave();
      palavrasChave.splice(i, 1);
      this.servico().palavrasChave(palavrasChave);
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
          ctrl.servico().palavrasChave().length <= 3 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m('.input-container', {
            class: (ctrl.servico().palavrasChave.erro() || [])[i]
          }, [
            m('input.inline[type=text]', {
              value: palavras,
              config: focus(ctrl),
              onchange: function (e) {
                var palavrasChave = ctrl.servico().palavrasChave();
                palavrasChave[i] = e.target.value;
                ctrl.servico().palavrasChave(palavrasChave);
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
