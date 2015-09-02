'use strict';

var focus = require('focus');
var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.documentos = args.campos;

    this.adicionar = function () {
      this.documentos().push(new modelos.Documento());
      this.adicionado = true;
    };

    this.remover = function (i) {
      this.documentos().splice(i, 1);
    };
  },

  view: function (ctrl, args) {
    if (ctrl.documentos().length === 0) {
      ctrl.adicionar();
    }

    return m('.documentos', [
      ctrl.documentos().map(function (documento, i) {
        return m('.documento.relative', {
          key: documento.id
        }, [
          ctrl.documentos().length === 1 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m('div.input-container', {
            class: documento.descricao.erro()
          }, [
            m('input[type=text]', {
              value: documento.descricao(),
              config: focus(ctrl),
              onchange: function (e) {
                documento.descricao(e.target.value);
              }
            })
          ])
        ]);
      }),
      m('button.adicionar.adicionar-documento', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar documentação '
      ])
    ]);
  }
};
