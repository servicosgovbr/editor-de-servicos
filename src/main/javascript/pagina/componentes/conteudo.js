'use strict';

var safeGet = require('utils/code-checks').safeGet;

module.exports = {
  view: function (ctrl, args) {
    var label = args.label;
    var conteudo = safeGet(args, 'prop');
    var maximo = safeGet(args, 'maximo');

    return m('fieldset#conteudo-pagina', [
      m('h3', [
        label || 'Conteúdo da página',
        m.component(args.tooltipConteudo)
      ]),
      m('.input-container', [
        m.component(require('componentes/editor-markdown'), {
          rows: 10,
          onchange: m.withAttr('value', conteudo),
          value: conteudo(),
          erro: conteudo.erro(),
          maximo: maximo
        })
      ])
    ]);
  }
};
