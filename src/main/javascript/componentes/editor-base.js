'use strict';

module.exports = {
  view: function (ctrl, args) {
    var conteudoConfig = args.config;
    var cabecalhoConfig = args.cabecalhoConfig;
    var menuLateralConfig = args.menuLateralConfig;

    var menuLateralComponente = args.menuLateralConfig ? m.component(require('componentes/menu/menu-lateral'), menuLateralConfig) : '';

    return m('#conteudo', {
      config: conteudoConfig
    }, [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('../cabecalho/cabecalho'), cabecalhoConfig),
        menuLateralComponente,
        m('#pagina',
          m('.scroll', args.componentes)
        )
      ])
   ]);
  }
};
