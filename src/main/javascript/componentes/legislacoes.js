'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().legislacoes().push(new modelos.Legislacao({}));
    };

    this.remover = function (i) {
      this.servico().legislacoes().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#legislacoes', [
      m('h3', [
        'Legislações relacionadas ao serviço',
        m.component(require('tooltips').legislacoes)
      ]),

      ctrl.servico().legislacoes().map(function (legislacao, i) {
        return m('.row', [
          m.component(require('componentes/select2'), {
            data: require('referencia').tiposDeLegislacao,
            prop: legislacao.tipo,
            minimumResultsForSearch: 1
          }),

          m('input.numero[type=text]', {
            value: legislacao.numero(),
            onchange: m.withAttr('value', legislacao.numero)
          }),

          m('span', ' de '),

          m('input.ano[type=text]', {
            value: legislacao.ano(),
            onchange: m.withAttr('value', legislacao.ano)
          }),

          m('input.complemento[type=text]', {
            value: legislacao.complemento(),
            onchange: m.withAttr('value', legislacao.complemento)
          }),

          m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-trash-o')
          ])
        ]);
      }),

      m('button.adicionar.adicionar-legislacao', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar legislação '
      ])
      ]);
  }
};
