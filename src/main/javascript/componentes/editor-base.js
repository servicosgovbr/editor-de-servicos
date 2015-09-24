'use strict';

module.exports = {
  view: function (ctrl, args) {
    var conteudoConfig = args.config;
    var cabecalhoConfig = args.cabecalhoConfig;

    return m('#conteudo', {
      config: conteudoConfig
    }, [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('../cabecalho/cabecalho'), cabecalhoConfig),
        m('#servico',
          m('.scroll', args.componentes)
        )
      ])
   ]);
  }
};
