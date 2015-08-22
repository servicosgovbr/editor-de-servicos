'use strict';

var referencia = require('referencia');
var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.canaisDePrestacao = args.campos;

    this.tiposDeCanalDePrestacao = m.prop(referencia.tiposDeCanalDePrestacao);

    this.adicionar = function () {
      this.canaisDePrestacao().push(new modelos.CanalDePrestacao());
    };

    this.remover = function (i) {
      this.canaisDePrestacao().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('.canais-de-prestacao', [
      ctrl.canaisDePrestacao().map(function (canalDePrestacao, i) {
        return m('.canal-de-prestacao', {
          key: canalDePrestacao.id
        }, [

          m.component(require('componentes/select2'), {
            data: ctrl.tiposDeCanalDePrestacao(),
            prop: ctrl.canaisDePrestacao()[i].tipo
          }),

          ctrl.canaisDePrestacao().length === 1 ? '' : m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [m('span')]),

          m('label.titulo', referencia.descricoesDeCanaisDePrestacao[ctrl.canaisDePrestacao()[i].tipo() || 'Descreva como o cidadão deve utilizar este canal']),
          m.component(require('componentes/editor-markdown'), {
            rows: 3,
            value: canalDePrestacao.descricao(),
            onchange: m.withAttr('value', canalDePrestacao.descricao)
          }),
        ]);
      }),
      m('button.adicionar.adicionar-canal-de-prestacao', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar canal '
      ])
    ]);
  }
};
