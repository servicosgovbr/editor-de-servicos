var EditorDeServicos = {

  controller: function () {
    this.servico = new models.Servico();

    this.debug = function () {
      console.log(JSON.stringify(this));
    };
  },

  view: function (ctrl) {
    return m('#principal.auto-grid', [
      m.component(DadosBasicos, {
        servico: ctrl.servico
      }),

      m.component(Solicitantes, {
        solicitantes: ctrl.servico.solicitantes()
      }),

      m.component(TempoTotalEstimado, {
        tempoTotalEstimado: ctrl.servico.tempoTotalEstimado()
      }),

      m.component(Etapas, {
        etapas: ctrl.servico.etapas()
      }),

      m.component(DadosComplementares, {
        servico: ctrl.servico
      }),

      m('button', {
        onclick: ctrl.debug.bind(ctrl.servico),
        style: {
          backgroundColor: '#d00'
        }
      }, [
        m("i.fa.fa-bug"),
        " Debug "
      ])
    ]);
  }
};
