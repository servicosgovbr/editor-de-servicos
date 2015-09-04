'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl, args) {
    var novo = args.novo;
    var servico = ctrl.servico();

    var componenteNome = novo ? m('input[type=text]', {
      onchange: m.withAttr('value', servico.nome),
      value: servico.nome(),
      autofocus: 'autofocus'
    }) : m('span', servico.nome());

    return m('fieldset#nome', [
      m('h2', 'dados básicos'),
      m('h3', [
        'Nome do serviço',
        m.component(require('tooltips').nome)
      ]),
      componenteNome
    ]);

  }

};
