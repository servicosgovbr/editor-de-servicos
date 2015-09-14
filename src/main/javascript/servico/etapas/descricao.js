'use strict';

module.exports = {
  view: function (ctrl, args) {
    var descricao = args.descricao;
    var indice = args.indice;

    return m('.descricao', [
      m('h3.opcional', [
        'Descrição da etapa ' + (indice + 1),
        m.component(require('tooltips').descricaoDaEtapa)
      ]),

      m.component(require('componentes/editor-markdown'), {
        rows: 3,
        onchange: m.withAttr('value', descricao),
        value: descricao(),
        erro: descricao.erro()
      })
    ]);
  }
};
