var DadosComplementares = {
  controller: function(args) {
    this.servico = args.servico;
  },
  view: function(ctrl) {
    return m('#dados-complementares', [
      m('h2', 'Dados Complementares'),

      m('fieldset', [
        m.component(OrgaoResponsavel, { servico: ctrl.servico }),
        m.component(SegmentosDaSociedade, { servico: ctrl.servico }),
        m.component(EventosDaLinhaDaVida, { servico: ctrl.servico }),
        m.component(AreasDeInteresse, { servico: ctrl.servico }),
        m.component(PalavrasChave, { servico: ctrl.servico }),
        m.component(Legislacoes, { servico: ctrl.servico })
      ])
    ]);
  }
};