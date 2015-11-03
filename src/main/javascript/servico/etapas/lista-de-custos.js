'use strict';

var modelos = require('../modelos');
var focus = require('focus');

module.exports = {

  controller: function (args) {
    this.adicionar = function (prop) {
      var custos = prop();
      custos.push(new modelos.Custo());
      prop(custos);
      this.adicionado = true;
    };

    this.remover = function (prop, i) {
      var custos = prop();
      custos.splice(i, 1);
      prop(custos);
    };
  },

  view: function (ctrl, args) {
    var custos = args.campos;
    if (custos().length === 0) {
      custos([new modelos.Custo()]);
    }

    return m('.custos', [
      custos().map(function (custo, i) {
        return m('.custo', {
          key: custo.id
        }, [
          m('.input-container.inline', {
            class: custo.descricao.erro()
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
          m('.input-container.inline', {
            class: custo.valor.erro()
          }, [m('input.valor[type=text]', {
            value: custo.valor(),
            placeholder: '0,00',
            onchange: m.withAttr('value', custo.valor)
          })]),
          ' ',
          custos().length === 1 ? m('') : m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [m('span')])
        ]);
      }),
      m('button.adicionar.adicionar-custo', {
        onclick: ctrl.adicionar.bind(ctrl, custos)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar custo '
      ])
    ]);
  }
};