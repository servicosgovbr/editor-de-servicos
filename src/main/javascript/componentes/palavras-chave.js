'use strict';

module.exports = {

  controller: function (args) {
    this.palavrasChave = args.palavrasChave;

    this.adicionar = function () {
      this.palavrasChave().push('');
    };

    this.remover = function (i) {
      this.palavrasChave().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#palavras-chave', [

      m('h3', 'Palavras-chave'),

      ctrl.palavrasChave().map(function (legislacao, i) {
        return [
          m('input.inline.inline-xg[type=text]', {
            value: legislacao,
            onchange: function (e) {
              ctrl.palavrasChave()[i] = e.target.value;
            }
          }),
          m('button.inline.remove-peq', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-times')
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
