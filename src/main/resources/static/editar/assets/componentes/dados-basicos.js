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
        m.component(Descricao, { descricao: ctrl.servico.descricao })
      ])
    ]);
  }
};
