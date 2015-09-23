'use strict';

module.exports = {
  view: function (ctrl, args) {
    return m('#conteudo', {
      config: args.config
    }, [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('../cabecalho/cabecalho'), {
          metadados: false,
          logout: true,
          salvar: _.noop,
          cabecalho: args.cabecalho
        }),
        m('#servico',
          m('.scroll', args.componentes)
        )
      ])
   ]);
  }
};
