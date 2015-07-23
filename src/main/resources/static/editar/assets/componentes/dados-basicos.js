var DadosBasicos = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('#dados-basicos', [
      m.component(Nome, { nome: ctrl.servico.nome }),
      m.component(NomesPopulares, { nomesPopulares: ctrl.servico.nomesPopulares }),
      m.component(Descricao, { descricao: ctrl.servico.descricao })
    ]);
  }
};
