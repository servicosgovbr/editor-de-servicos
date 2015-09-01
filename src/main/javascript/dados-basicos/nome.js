'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    var servico = ctrl.servico();

    return m('fieldset#nome', [
      m('h2', 'dados básicos'),
      m('h3', [
        'Nome do serviço',
        m.component(require('tooltips').nome)
      ]),

      m('div.input-container', {
        class: servico.nome.erro()
      }, [
        m('input[type=text]', {
          onchange: m.withAttr('value', servico.nome),
          value: servico.nome(),
          autofocus: 'autofocus'
        })
      ])
    ]);
  }
};
