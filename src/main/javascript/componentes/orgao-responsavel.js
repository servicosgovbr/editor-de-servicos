'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.todosOrgaos = m.request({
      method: 'GET',
      url: '/editar/api/orgaos'
    });
  },

  view: function (ctrl) {
    return m('fieldset#orgao-responsavel', [
      m('h3', 'Órgão responsável'),
      m('select', {
        onchange: m.withAttr('value', ctrl.servico().orgao)
      }, [m('option', {
        value: ''
      }, 'Selecione...')].concat(ctrl.todosOrgaos().map(function (orgao) {
        return m('option', {
          value: orgao.id,
          selected: ctrl.servico().orgao() === orgao.id
        }, orgao.nome);
      })))
    ]);
  }
};
