'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('#dados-basicos', [
      m.component(require('componentes/nome'), {
        nome: ctrl.servico.nome
      }),
      m.component(require('componentes/nomes-populares'), {
        nomesPopulares: ctrl.servico.nomesPopulares
      }),
      m.component(require('componentes/descricao'), {
        descricao: ctrl.servico.descricao
      }),
      m.component(require('componentes/tempo-total-estimado'), {
        tempoTotalEstimado: ctrl.servico.tempoTotalEstimado()
      }),
      m.component(require('componentes/gratuidade'), {
        gratuidade: ctrl.servico.gratuidade
      }),
    ]);
  }
};
