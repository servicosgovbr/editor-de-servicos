'use strict';

module.exports = {
  view: function (ctrl, args) {
    var servico = args.servico;

    return m('fieldset#descricao', [
      m('h3', [
        'Descrição do serviço',
        m.component(require('tooltips').descricao)
      ]),

      m.component(require('componentes/editor-markdown'), {
        rows: 6,
        onchange: m.withAttr('value', servico().descricao),
        value: servico().descricao(),
        erro: servico().descricao.erro()
      })
    ]);
  }
};
