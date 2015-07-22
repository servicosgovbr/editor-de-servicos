var OrgaoResponsavel = {
  controller: function(args) {
    this.servico = args.servico;
    this.orgaos = m.request({ method: 'GET', url: '/editar/api/orgaos' });
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Órgão responsável"),
      m("select", {
        onchange: function(e) { ctrl.servico.orgao(e.target.value); }
      }, [m('option', {value: ''}, 'Selecione...')].concat(ctrl.orgaos().map(function(orgao) {
        return m('option', {
          value: orgao.id,
          selected: ctrl.servico.orgao() === orgao.id
        }, orgao.nome);
      })))
    ]);
  }
};
