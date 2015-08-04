'use strict';

module.exports = {

  controller: function (args) {
    this.documentos = args.campos;

    this.adicionar = function () {
      this.documentos().push('');
    };

    this.remover = function (i) {
      this.documentos().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('.documentos', [
      ctrl.documentos().map(function (documento, i) {
        return m('.documento', {
          key: documento.id
        }, [
          m.component(require('componentes/editor-markdown'), {
            rows: 3,
            value: documento,
            onchange: function (e) {
              ctrl.documentos()[i] = e.target.value;
            }
          }),
          m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, m('span.fa.fa-trash-o'))
        ]);
      }),
      m('button.adicionar.adicionar-documento', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar documento '
      ])
    ]);
  }
};
