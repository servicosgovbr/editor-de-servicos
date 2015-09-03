'use strict';

var focus = require('focus');
var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.adicionar = function (prop) {
      var documentos = prop();
      documentos.push(new modelos.Documento());
      prop(documentos);

      this.adicionado = true;
    };

    this.remover = function (prop, i) {
      var documentos = prop();
      documentos.splice(i, 1);
      prop(documentos);
    };
  },

  view: function (ctrl, args) {
    var documentos = args.campos;
    if (documentos().length === 0) {
      documentos([new modelos.Documento()]);
    }

    return m('.documentos', [
      documentos().map(function (documento, i) {
        return m('.documento.relative', {
          key: documento.id
        }, [
          documentos().length === 1 ? '' : m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, documentos, i)
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
        onclick: ctrl.adicionar.bind(ctrl, documentos)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar documentação '
      ])
    ]);
  }
};
