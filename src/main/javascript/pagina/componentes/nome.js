'use strict';

module.exports = {
  view: function (ctrl, args) {
    var nomeProp = args.nome;
    var tooltipNome = args.tooltipNome;

    var componenteNome = args.novo ? m.component(args.componente, _.assign(args, {
      prop: nomeProp
    })) : nomeProp();

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
