'use strict';

module.exports = {
  view: function (ctrl, args) {
    var pagina = args.pagina();
    var tooltipNome = args.tooltipNome;

    var componenteNome = args.novo ? m.component(args.componente, {
      prop : args.nome
    }) : pagina.nome();

    return m('fieldset#nome', [
      m('h3', [
          'Órgão',
          args.novo
          ? m.component(tooltipNome)
          : ''
      ]),
      m('.input-container', componenteNome)
    ]);
  }
};
