'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.canaisDePrestacao = args.campos;

    this.tiposDeCanalDePrestacao = m.request({
      method: 'GET',
      url: '/editar/api/tipos-de-canal-de-prestacao'
    });

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
          m('select', {
            onchange: m.withAttr('value', ctrl.canaisDePrestacao()[i].tipo),
            style: {
              float: 'left'
            }
          }, [m('option', {
            value: ''
          }, 'Selecione...')].concat(ctrl.tiposDeCanalDePrestacao().map(function (tipo) {
            return m('option', {
              value: tipo,
              selected: ctrl.canaisDePrestacao()[i].tipo() === tipo
            }, tipo);
          }))),

          m('button.inline.remove-peq', {
            onclick: ctrl.remover.bind(ctrl, i),
            style: {
              float: 'right'
            }
          }, m('span.fa.fa-trash-o')),

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
