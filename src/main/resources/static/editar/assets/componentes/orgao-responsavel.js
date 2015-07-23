var OrgaoResponsavel = {
  controller: function(args) {
    this.orgao = args.orgao;
    this.orgaos = m.request({ method: 'GET', url: '/editar/api/orgaos' });
  },
  view: function(ctrl) {
    return m('', [
      m("h3", "Órgão responsável"),
      m("select", {
        onchange: function(e) { ctrl.orgao(e.target.value); }
      }, [m('option', {value: ''}, 'Selecione...')].concat(ctrl.orgaos().map(function(orgao) {
        return m('option', {
          value: orgao.id,
          selected: ctrl.orgao() === orgao.id
        }, orgao.nome);
      })))
    ]);
  }
};
