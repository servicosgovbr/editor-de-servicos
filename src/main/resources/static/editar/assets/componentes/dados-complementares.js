var DadosComplementares = {
  controller: function(args) {
    this.servico = args.servico;
  },
  view: function(ctrl) {
    return m('#dados-complementares', [
      m('h2', 'Dados Complementares'),

      m('fieldset', [
        m.component(OrgaoResponsavel, { orgao: ctrl.servico.orgao }),
        m.component(SegmentosDaSociedade, { segmentosDaSociedade: ctrl.servico.segmentosDaSociedade }),
        m.component(EventosDaLinhaDaVida, { eventosDaLinhaDaVida: ctrl.servico.eventosDaLinhaDaVida }),
        m.component(AreasDeInteresse, { areasDeInteresse: ctrl.servico.areasDeInteresse }),
        m.component(PalavrasChave, { palavrasChave: ctrl.servico.palavrasChave }),
        m.component(Legislacoes, { legislacoes: ctrl.servico.legislacoes })
      ])
    ]);
  }
};