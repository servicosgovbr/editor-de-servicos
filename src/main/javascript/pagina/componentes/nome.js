'use strict';

module.exports = {
  controller: function (args) {
    var pagina = args.pagina();
    this.nome = args.nomeFn(pagina.nome);
  },

  view: function (ctrl, args) {
    var tooltipNome = args.tooltipNome;

    var componenteNome = args.novo ? m.component(args.componente, {
      prop: args.nome
    }) : ctrl.nome();

    return m('fieldset#nome', [
      m('h3', [
          args.titulo,
          args.novo
          ? m.component(tooltipNome)
          : ''
      ]),
      m('.input-container', componenteNome)
    ]);
  }
};
