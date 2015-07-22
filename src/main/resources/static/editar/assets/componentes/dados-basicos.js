var DadosBasicos = {
  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('#dados-principais', [
      m('h2', 'Dados Principais'),

      m('fieldset', [
        m('h3', 'Nome do serviço'),
        m('input[type=text]', {
          onchange: m.withAttr('value', ctrl.servico.nome),
          value: ctrl.servico.nome()
        }),

        m('h3', 'Nomes populares'),
        m('input[type=text]', {
          onchange: m.withAttr('value', ctrl.servico.nomesPopulares),
          value: ctrl.servico.nomesPopulares()
        }),

        m('h3', 'Descrição do serviço'),
        m.component(EditorMarkdown, {
          rows: 10,
          oninput: m.withAttr('value', ctrl.servico.descricao),
          value: ctrl.servico.descricao()
        })
      ])
    ]);
  }
};
