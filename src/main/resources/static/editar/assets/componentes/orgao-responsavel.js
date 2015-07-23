var OrgaoResponsavel = {

  controller: function(args) {
    this.orgao = args.orgao;
    this.todosOrgaos = m.request({ method: 'GET', url: '/editar/api/orgaos' });
  },

  view: function(ctrl) {
    return m('fieldset#orgao-responsavel', [
      m("h3", "Órgão responsável"),
      m("select", {
        onchange: m.withAttr('value', ctrl.orgao)
      }, [m('option', {value: ''}, 'Selecione...')].concat(ctrl.todosOrgaos().map(function(orgao) {
        return m('option', {
          value: orgao.id,
          selected: ctrl.orgao() === orgao.id
        }, orgao.nome);
      })))
    ]);
  }
};
