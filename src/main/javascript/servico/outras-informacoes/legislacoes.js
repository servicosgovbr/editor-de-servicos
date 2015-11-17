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
    var erro = ctrl.servico().legislacoes.erro();
    var erroFieldSet = _.isString(erro) ? erro : '';
    var erroItens = _.isArray(erro) ? erro : [];

    return m('fieldset#legislacoes.relative', [
      m('h3.input-container', {
        class: erroFieldSet
      }, [
        'Legislações relacionadas ao serviço',
        m.component(require('tooltips').legislacoes)
      ]),

      ctrl.servico().legislacoes().map(function (legislacao, i) {
        return [
          ctrl.servico().legislacoes().length === 1 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m.component(require('componentes/editor-markdown'), {
            rows: 3,
            config: focus(ctrl),
            value: legislacao,
            erro: erroItens[i],
            onchange: function (e) {
              var valorNovo = e.target.value;
              var tmp = ctrl.servico().legislacoes();
              tmp[i] = valorNovo;
              ctrl.servico().legislacoes(tmp);
            }
          })
        ];
      }),
      m('button.adicionar.adicionar-legislacao', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar demais normativos '
      ])
    ]);
  }
};
