'use strict';

var modelos = require('modelos');
var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.custos = args.campos;

    this.adicionar = function () {
      this.custos().push(new modelos.Custo());
      this.adicionado = true;
    };

    this.remover = function (i) {
      this.custos().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('.custos', [
      ctrl.custos().map(function (custo, i) {
        custo.validar();

        return m('.custo', {
          key: custo.id
        }, [
          m('.input-container.inline', {
            class: custo.validador.hasError('descricao')
          }, [m('input.descricao[type=text]', {
            value: custo.descricao(),
            config: focus(ctrl),
            onchange: m.withAttr('value', custo.descricao)
          })]),
          ' ',
          m('.input-container.inline', [m('input.moeda[type=text][value="R$"]', {
            value: custo.moeda() || 'R$',
            placeholder: 'Unidade',
            onchange: m.withAttr('value', custo.moeda)
          })]),
          ' ',
          m('.input-container.inline', [m('input.valor[type=text]', {
            value: custo.valor(),
            placeholder: '0,00',
            onchange: m.withAttr('value', custo.valor)
          })]),
          ' ',
          ctrl.custos().length === 1 ? m('') : m('button.remove', {
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
