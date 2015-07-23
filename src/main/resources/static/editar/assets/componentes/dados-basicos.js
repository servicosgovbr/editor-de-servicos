var DadosBasicos = {
  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('#dados-principais', [
      m('h2', 'Dados Principais'),

      m('fieldset', [
        m.component(Nome, { nome: ctrl.servico.nome }),
        m.component(NomesPopulares, { nomesPopulares: ctrl.servico.nomesPopulares }),

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
