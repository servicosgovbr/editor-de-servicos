'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args;

    this.temSolicitante = function () {
      return !_.isEmpty(this.servico.solicitantes());
    };

    this.temEtapas = function () {
      return !_.isEmpty(this.servico.etapas());
    };

    this.temTempoEstimado = function () {
      return !_.isUndefined(this.servico.tempoTotalEstimado()) && (!_.isUndefined(this.servico.tempoTotalEstimado().ate) || !_.isUndefined(this.servico.tempoTotalEstimado().entre));
    };

    this.temLegislacao = function () {
      return !_.isEmpty(this.servico.legislacoes());
    };
  },

  view: function (ctrl) {
    return m('.ancoras', [m('ul', [
            m('li', [m('a', 'O que é?')]),
            ctrl.temSolicitante() ? m('li', [m('a', 'Quem pode utilizar este serviço?')]) : m(''),
            ctrl.temEtapas() ? m('li', [m('a', 'Etapas para a realização desde serviço')]) : m(''),
            ctrl.temTempoEstimado() ? m('li', [m('a', 'Quanto tempo leva?')]) : m(''),
            ctrl.temLegislacao() ? m('li', [m('a', 'Legislação')]) : m(''),
            m('li', [m('a', 'Outras informações')])
        ])]);
  }
};
