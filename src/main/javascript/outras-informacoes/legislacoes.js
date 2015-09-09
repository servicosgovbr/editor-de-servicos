'use strict';

var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      var legislacoes = this.servico().legislacoes();
      legislacoes.push('');

      this.servico().legislacoes(legislacoes);
      this.adicionado = true;
    };

    this.remover = function (i) {
      var legislacoes = this.servico().legislacoes();
      legislacoes.splice(i, 1);
      this.servico().legislacoes(legislacoes);
    };
  },

  view: function (ctrl) {
    return m('fieldset#legislacoes.relative', [
      m('h3', [
        'Legislações relacionadas ao serviço',
        m.component(require('tooltips').legislacoes)
      ]),

      ctrl.servico().legislacoes().map(function (legislacao, i) {
        return [
          ctrl.servico().legislacoes().length === 1 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m('.input-container', [
            m.component(require('componentes/editor-markdown'), {
              rows: 3,
              config: focus(ctrl),
              value: legislacao,
              onchange: function (e) {
                ctrl.servico().legislacoes()[i] = e.target.value;
              }
            })
          ])
        ];
      }),
      m('button.adicionar.adicionar-legislacao', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar lei, decreto ou portaria '
      ])
    ]);
  }
};
