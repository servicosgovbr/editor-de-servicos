'use strict';

var referencia = require('referencia');
var modelos = require('../modelos');

module.exports = {

  controller: function (args) {
    this.tiposDeCanalDePrestacao = m.prop(referencia.tiposDeCanalDePrestacao);

    this.adicionar = function (prop) {
      var canaisDePrestacao = prop();
      canaisDePrestacao.push(new modelos.CanalDePrestacao());
      prop(canaisDePrestacao);
    };

    this.remover = function (prop, i) {
      var canaisDePrestacao = prop();
      canaisDePrestacao.splice(i, 1);
      prop(canaisDePrestacao);
    };
  },

  view: function (ctrl, args) {
    var canaisDePrestacao = args.campos;
    if (canaisDePrestacao().length === 0) {
      ctrl.adicionar(canaisDePrestacao);
    }

    return m('.canais-de-prestacao', [
      canaisDePrestacao().map(function (canalDePrestacao, i) {
        return m('.canal-de-prestacao', {
          key: canalDePrestacao.id
        }, [

          m.component(require('componentes/select2'), {
            data: ctrl.tiposDeCanalDePrestacao(),
            prop: canalDePrestacao.tipo
          }),

          canaisDePrestacao().length === 1 ? '' : m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, canaisDePrestacao, i)
          }, [m('span')]),

          m('label.titulo', referencia.descricoesDeCanaisDePrestacao[canalDePrestacao.tipo() || 'Descreva como o cidadão deve utilizar este canal']),
          m.component(require('componentes/editor-markdown'), {
            rows: 3,
            value: canalDePrestacao.descricao(),
            onchange: m.withAttr('value', canalDePrestacao.descricao),
            erro: canalDePrestacao.descricao.erro()
          }),
        ]);
      }),
      m('button.adicionar.adicionar-canal-de-prestacao', {
        onclick: ctrl.adicionar.bind(ctrl, canaisDePrestacao)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar canal '
      ])
    ]);
  }
};
