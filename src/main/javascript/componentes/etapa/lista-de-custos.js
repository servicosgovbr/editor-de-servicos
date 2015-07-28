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
          m('input.inline.inline-lg[type=text]', {
            value: custo.descricao(),
            onchange: m.withAttr('value', custo.descricao)
          }),
          ' ',
          m('input.inline[type=text]', {
            value: custo.moeda(),
            onchange: m.withAttr('value', custo.moeda)
          }),
          ' ',
          m('input.inline[type=text]', {
            value: custo.valor(),
            onchange: m.withAttr('value', custo.valor)
          }),
          ' ',
          m('button.inline.remove-peq', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-times')
          ])
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
