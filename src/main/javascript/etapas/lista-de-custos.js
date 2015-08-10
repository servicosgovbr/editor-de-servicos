'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.custos = args.campos;

    this.adicionar = function () {
      this.custos().push(new modelos.Custo());
    };

    this.remover = function (i) {
      this.custos().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('.custos', [
      ctrl.custos().map(function (custo, i) {
        return m('.custo', {
          key: custo.id
        }, [
          m('input.descricao[type=text]', {
            value: custo.descricao(),
            onchange: m.withAttr('value', custo.descricao)
          }),
          ' ',
          m('input.moeda[type=text][value="R$"]', {
            value: custo.moeda() || 'R$',
            placeholder: 'Unidade',
            onchange: m.withAttr('value', custo.moeda)
          }),
          ' ',
          m('input.valor[type=text]', {
            value: custo.valor(),
            placeholder: '0,00',
            onchange: m.withAttr('value', custo.valor)
          }),
          ' ',
          m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [m('span')])
        ]);
      }),
      m('button.adicionar.adicionar-custo', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar custo '
      ])
    ]);
  }
};
